package e.helper;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.SequenceInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
    } catch (IOException | ParserConfigurationException
                                              | SAXException exception) {
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
    NodeList elements = document.getElementsByTagName("element");
    for (int i = 0; i < elements.getLength(); i++) {
      NodeList children = elements.item(i).getChildNodes();
      if (children.getLength() != 2) {
        throw new IllegalStateException(String.format(
             "<element> children:%d; expected 2", children.getLength()));
      }
      String remotePath = null, localPath = null;
      for (int i_property = 0; i_property < 2; i_property++) {
        Node property = (Element)children.item(i_property);
        Node attribute = property.getAttributes().item(0);
        switch (property.getNodeName()) {
        case "remote":
          remotePath = getPath(attribute);
          break;
        case "local":
          localPath = getPath(attribute);
          break;
        }
      }
      String name = remotePath
          .substring(remotePath.lastIndexOf('/') + 1);
      e.model.Element element = new e.model.Element(name,
                remotePath.substring(0, remotePath.lastIndexOf('/') + 1),
                 localPath.substring(0, localPath.lastIndexOf('/') + 1));
      O1027Elements.add(element);
    }
  }

  private static String getPath(Node node) {
    if (!"path".equals(node.getNodeName())) {
      throw new IllegalStateException();
    }
    return node.getNodeValue();
  }

  public static List<e.model.Element> getPropertyO1027Elements() {
    return O1027Elements;
  }
}