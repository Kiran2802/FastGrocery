
package apps.developer.fastgrocery.model.area;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AreaDataList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dcharge")
    @Expose
    private String dcharge;
    @SerializedName("status")
    @Expose
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDcharge() {
        return dcharge;
    }

    public void setDcharge(String dcharge) {
        this.dcharge = dcharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
