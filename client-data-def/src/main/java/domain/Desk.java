package domain;

public class Desk {
  private final String desk;
  private String description;
  
  public Desk(String desk) {
    this.desk = desk;
  }

  public String getDeskName() {
    return desk;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "Desk[" + desk +"]";
  }
}
