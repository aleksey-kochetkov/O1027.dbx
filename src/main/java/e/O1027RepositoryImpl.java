package e;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Repository;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import e.helper.ApplicationHelper;
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
      DbxAppInfo appInfo =
              new DbxAppInfo(ApplicationHelper.getPropertyO1027Key(),
                             ApplicationHelper.getPropertyO1027Secret());
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
                             ApplicationHelper.getPropertyO1027Key(),
                             ApplicationHelper.getPropertyO1027Secret());
    dbxClient = new DbxClientV2(dbxRequestConfig, dbxCredential);
  }
}
