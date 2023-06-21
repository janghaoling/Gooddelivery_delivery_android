package com.gooddelivery.delivery.activities;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gooddelivery.delivery.R;
import com.gooddelivery.delivery.helper.LocaleUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.gooddelivery.delivery.BuildConfigure.BASE_URL;

public class TermsAndConditions extends AppCompatActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        ButterKnife.bind(this);
        title.setText(getString(R.string.terms_and_conditions));
        webView.getSettings().setAppCacheEnabled(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BASE_URL+"terms");

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleUtils.onAttach(newBase));
    }
}
