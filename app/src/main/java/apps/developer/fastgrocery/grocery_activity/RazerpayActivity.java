package apps.developer.fastgrocery.grocery_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import apps.developer.fastgrocery.R;
import apps.developer.fastgrocery.Utils.SessionManager;
import apps.developer.fastgrocery.model.login_model.User;
import static apps.developer.fastgrocery.grocery_activity.Order_Summary.paymentsucsses;

public class RazerpayActivity extends AppCompatActivity  implements PaymentResultListener {
    SessionManager sessionManager;
    int amount = 0;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razerpay);

        sessionManager = new SessionManager(this);
        user = sessionManager.getUserDetails("");

        startPayment(String.valueOf(amount));

    }

    private void startPayment(String amount) {
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Fresh Fast Grocery Delivery");
            //You can omit the image option to fetch the image from dashboard
            options.put("currency", "INR");

            double total = Double.parseDouble(amount);
            total = total * 100;
            options.put("amount", total);

            JSONObject preFill = new JSONObject();
            preFill.put("email", user.getEmail());
            preFill.put("contact", user.getMobile());

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("onPaymentSuccess ", "-->" + s);
        paymentsucsses = 1;
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {

        Log.e("error", "-->" + i);
        Log.e("error", "-->" + s);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}