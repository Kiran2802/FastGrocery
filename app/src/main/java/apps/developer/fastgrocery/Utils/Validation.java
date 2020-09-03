package apps.developer.fastgrocery.Utils;



import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class Validation {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "[0-9]{10}";
    //    private static final String NAME_REGEX="[a-zA-Z]+\\.?";
    private static final String NAME_REGEX="^[\\p{L} .'-]+$";
    private static final String Pin_REGEX = "[0-9]{6}";
    private static final String CONSUMER_REGEX = "[0-9]{10}";
    private static final String BILLINGUNIT_REGEX = "[0-9]{4}";
   // private static final String PASSWORD_REGEX = "^[a-zA-Z0-9]*$";
    private static final String PASSWORD_REGEX = "^[a-zA-Z0-9@#$%]*$";
    //private static final String PASSWORD_REGEX =  "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{4,20})";
    private static final String GSTIN_REGEX = "^[a-zA-Z0-9]*$^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$";


   // private static final String DATE_PATTERN = "(0?[1-9]|1[012]) [/.-] (0?[1-9]|[12][0-9]|3[01]) [/.-] ((19|20)\\d\\d)";
     private static final String DATE_PATTERN = "^\\d{4}-\\d{2}-\\d{2}$";
     private static final String Time_PATTERN = "(([01]?[0-9]):([0-5][0-9]) ([AaPp][Mm]))";
   // private static final String Time_PATTERN = "^(1[0-2]|0?[1-9]):[0-5][0-9] (AM|PM)$";
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String EMAIL_MSG = "Invalid email";
    private static final String PIN_MSG = "Invalid number";
    private static final String PHONE_MSG = "Invalid number";
    private static final String NAME_MSG = "Invalid Name";
    private static final String password_Msg = "Invalid password";
    private static final String Gst_Msg = "Invalid number";
    private static final String Date_Msg = "Invalid Date";
    private static final String Time_Msg = "Invalid Time";

    // call this method when you need to check email validation
    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    // call this method when you need to check phone number validation
    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }
    public static boolean isName(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, NAME_MSG, required);
    }

    public static boolean isPassword(EditText editText, boolean required) {
        return isValid(editText, PASSWORD_REGEX, password_Msg, required);
    }
    public static boolean isPinCode(EditText editText, boolean required) {
        return isValid(editText, Pin_REGEX, PIN_MSG, required);
    }

    public static boolean isGstCode(EditText editText, boolean required) {
        return isValid(editText, GSTIN_REGEX, Gst_Msg, required);
    }

    public static boolean isDateCode(EditText editText, boolean required) {
        return isValid(editText, DATE_PATTERN, Date_Msg, required);
    }

    public static boolean isTimeCode(EditText editText, boolean required) {
        return isValid(editText, Time_PATTERN, Time_Msg, required);
    }
    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);
        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }

    public static boolean hasTextView(TextView textView) {

        String text = textView.getText().toString().trim();
        textView.setError(null);
        if (text.length() == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        }
        return true;
    }

}