package e;

import java.io.OutputStream;
import java.io.FileOutputStream;
import org.springframework.stereotype.Repository;
import e.model.Element;

@Repository
public class O1027RepositoryImpl implements O1027Repository {
  @Override
  public void download(Element element) {
    try (OutputStream
           outputStream = new FileOutputStream(element.getLocalPath())) {
      dbxClient.files().downloadBuilder(element.getPath())
                                                 .download(outputStream);
    } catch (IOException | DbxException exception) {
      throw new RuntimeException(exception);
    }
    LOGGER.info("download() {} {}",
                              element.getLocalPath(), element.getPath());
  }
}
