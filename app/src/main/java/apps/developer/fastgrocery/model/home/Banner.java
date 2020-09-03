
package apps.developer.fastgrocery.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("bimg")
    @Expose
    private String bimg;

    public Banner(String id, String bimg) {
        this.id = id;
        this.bimg = bimg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBimg() {
        return bimg;
    }

    public void setBimg(String bimg) {
        this.bimg = bimg;
    }

}
