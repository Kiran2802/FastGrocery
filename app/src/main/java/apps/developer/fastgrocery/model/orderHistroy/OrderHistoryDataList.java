
package apps.developer.fastgrocery.model.orderHistroy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryDataList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("oid")
    @Expose
    private String oid;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("total")
    @Expose
    private String total;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
