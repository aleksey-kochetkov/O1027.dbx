package e;

import java.io.File;
import java.util.Objects;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileSystemUtils;
import org.zeroturnaround.zip.ZipUtil;
import e.helper.StringHelper;
import e.helper.ApplicationHelper;
import e.model.Element;

@Service
public class Gp {
  private static final Logger LOGGER = LoggerFactory.getLogger(Gp.class);
  private static final Logger SL =
                        LoggerFactory.getLogger("e.helper.SimpleLogger");
  @Autowired
  private O1027Repository repository;

  public void download() {
    for (Element
          element : ApplicationHelper.getPropertyO1027Elements()) {
      repository.get(element);
      final long local = new File(element.getLocalPath()).lastModified();
      final long remote = element.getTime();
      if (local >= remote) {
        SL.warn("skip {} {} > {}", element.getLocalPath(),
                                  StringHelper.toString(new Date(local)),
                                StringHelper.toString(new Date(remote)));
      } else {
        repository.download(element);
        unzip(element);
      }
    }
  }

  public void upload() {
    for (Element
          element : ApplicationHelper.getPropertyO1027Elements()) {
      repository.get(element);
      long local = new File(element.getLocalPath()).lastModified();
      final long remote = element.getTime();
      final int d = 1000 - (int)(local % 1000);
      if (d < 1000) {
        local += d;
        ApplicationHelper.setLastModified(element.getLocalPath(), local);
      }
      element.setLocalTime(local);
      if (local <= remote) {
        SL.warn("skip {} {} < {}", element.getLocalPath(),
                                  StringHelper.toString(new Date(local)),
                                StringHelper.toString(new Date(remote)));
      } else {
        zip(element);
        repository.upload(element);
      }
    }
  }

  private void zip(Element element) {
    if (!element.isZip()) {
      return;
    }
    final File local = new File(element.getLocalPath());
    ZipUtil.pack(local, new File(element.getTmpPath()));
  }

  private void unzip(Element element) {
    if (!element.isZip()) {
      return;
    }
    String str = element.getLocalPath();
    str = str.substring(0, str.length() - 1) + ".tmp/";
    FileSystemUtils.deleteRecursively(new File(str));
    new File(element.getLocalPath()).renameTo(new File(str));
    final File zip = new File(element.getTmpPath()); 
    ZipUtil.unpack(zip, new File(element.getLocalPath()));
    ApplicationHelper.setLastModified(element.getLocalPath(),
                                                      element.getTime());
    if (element.isCleanTmp()) {
      zip.delete();
    }
    SL.info("unzip() {}", element.getLocalPath());
  }
}
