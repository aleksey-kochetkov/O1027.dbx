package e.model;

import java.util.Objects;
import java.util.Date;

public class Element {
  private String name;
  private String remoteFolder;
  private String localFolder;
  private Date remoteDate;
  private Date localDate;

  public Element(String name, String remoteFolder, String localFolder) {
    this.name = name;
    this.remoteFolder = remoteFolder;
    this.localFolder = localFolder;
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
    return remoteFolder + name;
  }

  public String getLocalPath() {
    return localFolder + name;
  }

  @Override
  public String toString() {
    return String.format("Element{%s}", Objects.toString(name));
  }
}
