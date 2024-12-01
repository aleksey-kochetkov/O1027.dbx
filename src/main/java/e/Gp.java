package e;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import e.helper.ApplicationHelper;
import e.model.Element;

@Service
public class Gp {
  private static final Logger LOGGER = LoggerFactory.getLogger(Gp.class);
  @Autowired
  private O1027Repository repository;

  public void download() {
    for (Element element : ApplicationHelper.getPropertyO1027Elements()) {
      repository.get(element);
      repository.download(element);
    }
  }
}
