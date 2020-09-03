
package apps.developer.fastgrocery.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import apps.developer.fastgrocery.model.login_model.User;

public class LoginResponseData {

    @SerializedName("user")
    @Expose
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
