
package apps.developer.fastgrocery.model.home;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Productlist implements Parcelable {

    @SerializedName("id")
    private String mId;
    @SerializedName("price")
    private ArrayList<Price> mPrice;
    @SerializedName("product_image")
    private String mProductImage;
    @SerializedName("product_name")
    private String mProductName;
    @SerializedName("seller_name")
    private String mSellerName;
    @SerializedName("short_desc")
    private String mShortDesc;
    @SerializedName("stock")
    private String mStock;
    @SerializedName("discount")
    private int mDiscount;

    protected Productlist(Parcel in) {
        mId = in.readString();
        mProductImage = in.readString();
        mProductName = in.readString();
        mSellerName = in.readString();
        mShortDesc = in.readString();
        mStock = in.readString();
        mDiscount = in.readInt();
    }

    public static final Creator<Productlist> CREATOR = new Creator<Productlist>() {
        @Override
        public Productlist createFromParcel(Parcel in) {
            return new Productlist(in);
        }

        @Override
        public Productlist[] newArray(int size) {
            return new Productlist[size];
        }
    };

    public int getmDiscount() {
        return mDiscount;
    }

    public void setmDiscount(int mDiscount) {
        this.mDiscount = mDiscount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public ArrayList<Price> getPrice() {
        return mPrice;
    }

    public void setPrice(ArrayList<Price> price) {
        mPrice = price;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String productImage) {
        mProductImage = productImage;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getSellerName() {
        return mSellerName;
    }

    public void setSellerName(String sellerName) {
        mSellerName = sellerName;
    }

    public String getShortDesc() {
        return mShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        mShortDesc = shortDesc;
    }

    public String getStock() {
        return mStock;
    }

    public void setStock(String stock) {
        mStock = stock;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mProductImage);
        dest.writeString(mProductName);
        dest.writeString(mSellerName);
        dest.writeString(mShortDesc);
        dest.writeString(mStock);
        dest.writeInt(mDiscount);
    }
}
