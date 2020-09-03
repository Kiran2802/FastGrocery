
package apps.developer.fastgrocery.model.timeslot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeslotDataList {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("mintime")
    @Expose
    private String mintime;
    @SerializedName("maxtime")
    @Expose
    private String maxtime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMintime() {
        return mintime;
    }

    public void setMintime(String mintime) {
        this.mintime = mintime;
    }

    public String getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(String maxtime) {
        this.maxtime = maxtime;
    }

}
