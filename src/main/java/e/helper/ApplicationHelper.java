package e.helper;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class ApplicationHelper {
  private static List<e.model.Element> O1027Elements = new ArrayList<>();

  static {
    init();
  }

  public static void init() {
    initFileProperties();
    try {
      initFileElements();
    } catch (IOException | ParserConfigurationException exception) {
      throw new RuntimeException(exception);
    }
  }

  private static void initFileProperties() {
  }

  private static void initFileElements()
                        throws IOException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
  }

  public static List<e.model.Element> getPropertyO1027Elements() {
    return O1027Elements;
  }
}