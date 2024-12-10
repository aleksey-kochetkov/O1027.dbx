package e.model;

import java.util.Objects;
import java.util.Date;

public class Element {
  private String name;
  private String remotePath;
  private String localPath;
  private Date remoteDate;
  private Date localDate;
  private boolean cleanTmp;

  public Element(String name,
                 String remotePath, String localPath, boolean cleanTmp) {
    this.name = name;
    this.remotePath = remotePath;
    this.localPath = localPath;
    this.cleanTmp = cleanTmp;
  }

  public String getName() {
    return name;
  }

  public void setDate(Date date) {
    this.remoteDate = date;
  }

  public long getTime() {
    return remoteDate == null ? 0 : remoteDate.getTime();
  }

  public Date getLocalDate() {
    return localDate;
  }

  public void setLocalTime(long localTime) {
    localDate = new Date(localTime);
  }

  public String getPath() {
    return remotePath;
  }

  public String getLocalPath() {
    return localPath;
  }

  public String getTmpPath() {
    return localPath.substring(0, localPath.length() - 1) + ".zip";
  }

  public boolean isZip() {
    return name.endsWith(".zip");
  }

  public boolean isCleanTmp() {
    return cleanTmp;
  }

  @Override
  public String toString() {
    return String.format("Element{%s}", Objects.toString(name));
  }
}
