package application.models;

public enum MessageFileType {
  NONE("NONE"),
  FILE("FILE"),
  GIF("GIF"),
  IMAGE("IMAGE");

  private String _data;

  private MessageFileType(String data) {
    this._data = data;
  }

  public String toString() {
    return _data;
  }
}
