package e.helper;

import java.io.File;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.SequenceInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

@Component
public class ApplicationHelper {
  private static final Logger LOGGER =
                        LoggerFactory.getLogger(ApplicationHelper.class);
  private static final String FILE_PROPERTIES = "O1027.properties";
  private static final String FILE_ELEMENTS = "elements.xml";
  private static String O1027Key;
  private static String O1027Secret;
  private static String O1027AccessToken;
  private static Long O1027ExpiresAt;
  private static String O1027RefreshToken;
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

  private static void initFileProperties() {
    Properties file = new Properties();
    try (FileInputStream in = new FileInputStream(FILE_PROPERTIES)) {
      file.load(in);
      O1027AccessToken = file.getProperty("O1027.access-token");
      O1027ExpiresAt =
                      Long.valueOf(file.getProperty("O1027.expires-at"));
      O1027RefreshToken = file.getProperty("O1027.refresh-token");
    } catch (Throwable exception) {
      LOGGER.error(StringHelper.getStackTrace(exception));
      try (FileInputStream in = new FileInputStream(
                                     System.getProperty("java.io.tmpdir")
                               + File.separatorChar + FILE_PROPERTIES)) {
        file.load(in);
        O1027AccessToken = file.getProperty("O1027.access-token");
        O1027ExpiresAt =
                      Long.valueOf(file.getProperty("O1027.expires-at"));
        O1027RefreshToken = file.getProperty("O1027.refresh-token");
      } catch (Throwable exc) {
        LOGGER.error(StringHelper.getStackTrace(exc));
      }
    }
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
        Node property = children.item(i_property);
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

  private static
            void setApplicationContext_internal(ApplicationContext ctx) {
    O1027Key = ctx.getEnvironment().getProperty("O1027.key");
    O1027Secret = ctx.getEnvironment().getProperty("O1027.secret");
  }

  @Autowired
  public void setApplicationContext(ApplicationContext ctx) {
    setApplicationContext_internal(ctx);
  }

  public static void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException exception) {
      LOGGER.error(StringHelper.getStackTrace(exception));
    }
  }

  @SafeVarargs
  public static <T> Enumeration<T> enumeration(T... args) {
    return Collections.enumeration(Arrays.asList(args));
  }

  public static InputStream newSequenceInputStream(
                  String openingTag, InputStream in, String closingTag) {
    return new SequenceInputStream(enumeration(
            (InputStream)new ByteArrayInputStream(openingTag.getBytes()),
                   in, new ByteArrayInputStream(closingTag.getBytes())));
  }

  public static void storeO1027Properties() {
    Properties file = new Properties();
    file.setProperty("O1027.access-token",
                                          getPropertyO1027AccessToken());
    file.setProperty("O1027.expires-at",
                          Objects.toString(getPropertyO1027ExpiresAt()));
    file.setProperty("O1027.refresh-token",
                                         getPropertyO1027RefreshToken());
    try (FileOutputStream out = new FileOutputStream(FILE_PROPERTIES)) {
      file.store(out, null);
    } catch (Throwable exception) {
      LOGGER.error(StringHelper.getStackTrace(exception));
      try (FileOutputStream out = new FileOutputStream(
                                     System.getProperty("java.io.tmpdir")
                               + File.separatorChar + FILE_PROPERTIES)) {
        file.store(out, null);
      } catch (Throwable exc) {
        LOGGER.error(StringHelper.getStackTrace(exc));
      }
    }
  }

  public static String getPropertyO1027Key() {
    return O1027Key;
  }

  public static String getPropertyO1027Secret() {
    return O1027Secret;
  }

  public static String getPropertyO1027AccessToken() {
    return O1027AccessToken;
  }

  public static
              void setPropertyO1027AccessToken(String O1027AccessToken) {
    ApplicationHelper.O1027AccessToken = O1027AccessToken;
  }

  public static Long getPropertyO1027ExpiresAt() {
    return O1027ExpiresAt;
  }

  public static void setPropertyO1027ExpiresAt(Long O1027ExpiresAt) {
    ApplicationHelper.O1027ExpiresAt = O1027ExpiresAt;
  }

  public static String getPropertyO1027RefreshToken() {
    return O1027RefreshToken;
  }

  public static void setPropertyO1027RefreshToken(String O1027RefreshToken) {
    ApplicationHelper.O1027RefreshToken = O1027RefreshToken;
  }

  public static List<e.model.Element> getPropertyO1027Elements() {
    return O1027Elements;
  }
}