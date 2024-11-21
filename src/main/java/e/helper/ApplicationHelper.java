package e.helper;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class ApplicationHelper {
  private static final String FILE_ELEMENTS = "elements.xml";
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

  public static <T> Enumeration<T> enumeration(T... args) {
    return Collections.enumeration(Arrays.asList(args));
  }

  public static InputStream newSequenceInputStream(
                  String openingTag, InputStream in, String closingTag) {
    return new SequenceInputStream(enumeration(
            (InputStream)new ByteArrayInputStream(openingTag.getBytes()),
                   in, new ByteArrayInputStream(closingTag.getBytes())));
  }

  private static void initFileProperties() {
  }

  private static void initFileElements()
         throws IOException, ParserConfigurationException, SAXException {
    DocumentBuilderFactory
        factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(newSequenceInputStream("<file>",
                         new FileInputStream(FILE_ELEMENTS), "</file>"));
  }

  public static List<e.model.Element> getPropertyO1027Elements() {
    return O1027Elements;
  }
}