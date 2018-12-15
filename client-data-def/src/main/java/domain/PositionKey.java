package domain;

public class PositionKey {
  private String desk;
  private String product;
  
  public PositionKey(String desk, String product) {
    this.desk = desk;
    this.product = product;
  }

  public String getDesk() {
    return desk;
  }

  public String getProduct() {
    return product;
  }

  @Override
  public String toString() {
    return "PositionKey[" + desk + "," + product + "]";
  }
}
