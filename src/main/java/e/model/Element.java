package e.model;

import java.util.Date;

public class Element {
  private String name;
  private String remoteFolder;
  private String localFolder;
  private Date date;

  public Element(String name, String remoteFolder, String localFolder) {
    this.name = name;
    this.remoteFolder = remoteFolder;
    this.localFolder = localFolder;
  }

  public long getTime() {
    return date == null ? 0 : date.getTime();
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getPath() {
    return remoteFolder + name;
  }

  public String getLocalPath() {
    return localFolder + name;
  }
}
