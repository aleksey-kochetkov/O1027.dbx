package e.model;

public class Element {
  private String name;
  private String remoteFolder;
  private String localFolder;

  public Element(String name, String remoteFolder, String localFolder) {
    this.name = name;
    this.remoteFolder = remoteFolder;
    this.localFolder = localFolder;
  }
}
