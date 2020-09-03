
package apps.developer.fastgrocery.model.home;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultData {

    @SerializedName("Banner")
    @Expose
    private List<Banner> banner = null;
    @SerializedName("Catlist")
    @Expose
    private List<Catlist> catlist = null;
    @SerializedName("Productlist")
    @Expose
    private List<Productlist> productlist = null;
    @SerializedName("Remain_notification")
    @Expose
    private Integer remainNotification;
    @SerializedName("currency")
    @Expose
    private String currency;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Catlist> getCatlist() {
        return catlist;
    }

    public void setCatlist(List<Catlist> catlist) {
        this.catlist = catlist;
    }

    public List<Productlist> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<Productlist> productlist) {
        this.productlist = productlist;
    }

    public Integer getRemainNotification() {
        return remainNotification;
    }

    public void setRemainNotification(Integer remainNotification) {
        this.remainNotification = remainNotification;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

}
