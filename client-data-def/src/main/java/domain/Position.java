package domain;

public class Position {
  private String desk;
  private String product;
  private long quantity;
  
  public Position(String desk, String product) {
    this.desk = desk;
    this.product = product;
  }

  public String getDesk() {
    return desk;
  }

  public String getProduct() {
    return product;
  }

  public long getQuantity() {
    return quantity;
  }

  @Override
  public String toString() {
    return "Position[" + desk + "," + product + "," + quantity + "]";
  }
}
