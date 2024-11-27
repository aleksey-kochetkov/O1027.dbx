package e;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import e.model.Element;

@Repository
public class O1027RepositoryImpl implements O1027Repository {
  private static final Logger LOGGER =
                      LoggerFactory.getLogger(O1027RepositoryImpl.class);
  private DbxClientV2 dbxClient;

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

  @Override
  public void upload(Element element) {
    try (InputStream in = new FileInputStream(element.getLocalPath())) {
      FileMetadata metadata = dbxClient.files()
               .uploadBuilder(element.getPath()).uploadAndFinish(in);
    } catch (IOException | DbxException exception) {
      throw new RuntimeException(exception);
    }
    LOGGER.info("{} {}", element.getLocalPath(), element.getPath());
  }

  @PostConstruct
  public void init() {
    DbxRequestConfig dbxRequestConfig = DbxRequestConfig
                                            .newBuilder("O1027").build();
    DbxCredential dbxCredential;
    if (ApplicationHelper.getPropertyO1027RefreshToken() == null) {
      DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
      DbxWebAuth webAuth = new DbxWebAuth(dbxRequestConfig, appInfo);
      DbxWebAuth.Request.Builder
                                builder = DbxWebAuth.newRequestBuilder();
      DbxWebAuth.Request authRequest = builder
                   .withTokenAccessType(TokenAccessType.OFFLINE).build();
      String authorizeUrl = webAuth.authorize(authRequest);
      System.out.println("1. Go to: " + authorizeUrl);
      System.out
         .println("2. Click \"Allow\" (you might have to log in first)");
      System.out.println("3. Copy the authorization code");
      String code = new Scanner(System.in).nextLine().trim();
      DbxAuthFinish authFinish;
      try {
        authFinish = webAuth.finishFromCode(code);
      } catch (DbxException exception) {
        throw new RuntimeException(exception);
      }
      ApplicationHelper
               .setPropertyO1027AccessToken(authFinish.getAccessToken());
      ApplicationHelper
                   .setPropertyO1027ExpiresAt(authFinish.getExpiresAt());
      ApplicationHelper
             .setPropertyO1027RefreshToken(authFinish.getRefreshToken());
      ApplicationHelper.storeO1027Properties();
    }
    dbxCredential = new DbxCredential(
                         ApplicationHelper.getPropertyO1027AccessToken(),
                           ApplicationHelper.getPropertyO1027ExpiresAt(),
                        ApplicationHelper.getPropertyO1027RefreshToken(),
                                                    APP_KEY, APP_SECRET);
    dbxClient = new DbxClientV2(dbxRequestConfig, dbxCredential);
  }
}
