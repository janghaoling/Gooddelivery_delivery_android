package com.gooddelivery.delivery.helper;

import android.graphics.Typeface;
import android.location.Location;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.gooddelivery.delivery.api.ApiClient;
import com.gooddelivery.delivery.api.ApiInterface;
import com.gooddelivery.delivery.model.Order;
import com.gooddelivery.delivery.model.Otp;
import com.gooddelivery.delivery.model.Profile;
import com.gooddelivery.delivery.model.Shift;
import com.gooddelivery.delivery.model.Token;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Created by santhosh@appoets.com on 27/09/2017.
 */

public class GlobalData {
    public static Otp otp;
    public static Token token;
    public static Profile profile;
    public static Shift shift;
    public static Order order;
    public static ApiInterface api = ApiClient.getRetrofit().create(ApiInterface.class);
    public static List<String> ORDER_STATUS = Arrays.asList("ORDERED", "RECEIVED", "ASSIGNED", "PROCESSING", "REACHED", "PICKEDUP", "ARRIVED", "COMPLETED");
    public static Location CURRENT_LOCATION = null;
    public static  Boolean isShiftOn;


    public static NumberFormat getNumberFormat() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        if(GlobalData.profile != null && GlobalData.profile.getCurrencyCode() != null){
            numberFormat.setCurrency(Currency.getInstance(profile.getCurrencyCode()));
        }else{
            numberFormat.setCurrency(Currency.getInstance("USD"));
        }
        numberFormat.setMinimumFractionDigits(0);
        return numberFormat;
    }

    public static String getTimeFromString(String time) {
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

            if (!time.isEmpty()) {
                value = sdf.format(df.parse(time));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getTimeFromString2(String time) {
        String value = "";
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault());

            if (!time.isEmpty()) {
                value = sdf.format(df.parse(time));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getNextOrderStatus(String value) {
        int id = ORDER_STATUS.indexOf(value);
        if (id < 0 || id + 1 == ORDER_STATUS.size()) return "";
        return ORDER_STATUS.get(id + 1);
    }

    public String getPreviousOrderStatus(String value) {
        int id = ORDER_STATUS.indexOf(value);
        if (id <= 0) return "";
        return ORDER_STATUS.get(id - 1);
    }

    private static CharSequence apply(CharSequence[] content, Object... tags) {
        SpannableStringBuilder text = new SpannableStringBuilder();
        openTags(text, tags);
        for (CharSequence item : content) {
            text.append(item);
        }
        closeTags(text, tags);
        return text;
    }
    private static void openTags(Spannable text, Object[] tags) {
        for (Object tag : tags) {
            text.setSpan(tag, 0, 0, Spannable.SPAN_MARK_MARK);
        }
    }
    private static void closeTags(Spannable text, Object[] tags) {
        int len = text.length();
        for (Object tag : tags) {
            if (len > 0) {
                text.setSpan(tag, 0, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                text.removeSpan(tag);
            }
        }
    }
    public static CharSequence bold(CharSequence... content) {
        return apply(content, new StyleSpan(Typeface.BOLD));
    }
    public static CharSequence color(int color, CharSequence... content) {
        return apply(content, new ForegroundColorSpan(color));
    }

}
