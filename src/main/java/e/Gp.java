package e;

import java.io.File;
import java.util.Objects;
import java.util.Date;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import e.helper.StringHelper;
import e.helper.ApplicationHelper;
import e.model.Element;

@Service
public class Gp {
  private static final Logger LOGGER = LoggerFactory.getLogger(Gp.class);
  @Autowired
  private O1027Repository repository;

  public void download() {
    for (Element
          element : ApplicationHelper.getPropertyO1027Elements()) {
      repository.get(element);
      final long local = new File(element.getLocalPath()).lastModified();
      final long remote = element.getTime();
      if (local > remote) {
        LOGGER.warn("skip {} {} > {}", element.getLocalPath(),
                                  StringHelper.toString(new Date(local)),
                                StringHelper.toString(new Date(remote)));
      } else {
        repository.download(element);
      }
    }
  }

  public void upload() {
  }
}
