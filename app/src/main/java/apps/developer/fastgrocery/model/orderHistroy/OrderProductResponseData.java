
package apps.developer.fastgrocery.model.orderHistroy;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderProductResponseData {

    @SerializedName("productinfo")
    @Expose
    private List<Productinfo> productinfo = null;
    @SerializedName("Sub_total")
    @Expose
    private double subTotal;
    @SerializedName("orderid")
    @Expose
    private String orderid;
    @SerializedName("total_amt")
    @Expose
    private double totalAmt;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("order_date")
    @Expose
    private String orderDate;
    @SerializedName("timesloat")
    @Expose
    private String timesloat;
    @SerializedName("Israted")
    @Expose
    private String israted;
    @SerializedName("d_charge")
    @Expose
    private String dCharge;
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;

    public List<Productinfo> getProductinfo() {
        return productinfo;
    }

    public void setProductinfo(List<Productinfo> productinfo) {
        this.productinfo = productinfo;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(double totalAmt) {
        this.totalAmt = totalAmt;
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

    public String getTimesloat() {
        return timesloat;
    }

    public void setTimesloat(String timesloat) {
        this.timesloat = timesloat;
    }

    public String getIsrated() {
        return israted;
    }

    public void setIsrated(String israted) {
        this.israted = israted;
    }

    public String getDCharge() {
        return dCharge;
    }

    public void setDCharge(String dCharge) {
        this.dCharge = dCharge;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

}
