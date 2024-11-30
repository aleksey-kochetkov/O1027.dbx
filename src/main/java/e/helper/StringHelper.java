package e.helper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class StringHelper {
  private static final SimpleDateFormat F =
                                new SimpleDateFormat("yyyy-MM-dd HH:mm");

  public static String toString(Date value) {
    return value == null ? null : F.format(value);
  }

  public static String getStackTrace(Throwable throwable) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            throwable.printStackTrace(new PrintStream(out));
            String str = out.toString();
            StringBuilder sb = new StringBuilder();
            boolean found = false;
            try (Scanner scanner = new Scanner(str)) {
                while (scanner.hasNextLine()) {
                    String tmp = scanner.nextLine();
                    if (sb.length() == 0) {
                        sb.append(tmp);
                    } else if (tmp.contains("at e.")) {
                        sb.append(System.lineSeparator());
                        sb.append(tmp);
                        found = true;
                        break;
                    }
                }
            }
            if (found) {
                return sb.toString();
            } else {
                return str;
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
