package domain;

import java.util.Date;

public class Product {
  private final String pmsCode;
  
  private String description;
  private String assetType;
  private Date updateTime;
  
  public Product(String pmsCode ) {
    this.pmsCode = pmsCode;
  }
  
  public String getPmsCode() {
    return pmsCode;
  }
  
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAssetType() {
    return assetType;
  }

  public void setAssetType(String assetType) {
    this.assetType = assetType;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  @Override
  public String toString() {
    return "Product[" + pmsCode + "," + description + "," + assetType + "," + updateTime + "]";
  }
  
}
