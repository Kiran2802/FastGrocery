
package apps.developer.fastgrocery.model.orderHistroy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Productinfo {

    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("discount")
    @Expose
    private int discount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

}
