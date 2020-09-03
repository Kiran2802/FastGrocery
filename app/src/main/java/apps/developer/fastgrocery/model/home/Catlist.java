
package apps.developer.fastgrocery.model.home;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Catlist {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("catname")
    @Expose
    private String catname;
    @SerializedName("catimg")
    @Expose
    private String catimg;
    @SerializedName("count")
    @Expose
    private Integer count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getCatimg() {
        return catimg;
    }

    public void setCatimg(String catimg) {
        this.catimg = catimg;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
