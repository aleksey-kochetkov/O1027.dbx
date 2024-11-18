package e;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class O1027 implements CommandLineRunner {
  private static final Logger LOGGER = LoggerFactory.getLogger(O1027.class);
  @Autowired
  private Gp gp;

  public static void main(String[] args) {
    SpringApplication.run(O1027.class, args);
  }

  @Override
  public void run(String[] args) {
    if (args.length == 1 && args[0].equals("--no-operation")) {
      LOGGER.info("run(): test launch, no operation requested");
    } else if (args.length == 1 && (
                 args[0].equals("--download") || args[0].equals("-d"))) {
      gp.download();
    }
  }
}
