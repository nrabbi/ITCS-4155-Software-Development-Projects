// Nazmul Rabbi
// ITCS 4155 : Event Finder
// webView.java
// Group 12
// 3/20/18

package com.example.nrabbi.itcs4155;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class webView extends AppCompatActivity {
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getIntent().getStringExtra("title"));
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);
        String url = getIntent().getStringExtra("url");
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onLoadResource(WebView view, String url) {
                view.loadUrl("javascript:document.getElementById('header').remove();");
                view.loadUrl("javascript:document.getElementById('mobile-location-bar').remove();");
                view.loadUrl("javascript:document.getElementById('adSlot1').remove();");
                view.loadUrl("javascript:document.getElementById('adSlot2').remove();");
                view.loadUrl("javascript:document.getElementById('adSlot3').remove();");
                view.loadUrl("javascript:document.getElementById('tab-nav').remove();");
                view.loadUrl("javascript:document.getElementById('facepile').remove();");
                view.loadUrl("javascript:document.getElementById('facepile-header').remove();");
                view.loadUrl("javascript:document.getElementById('footer').remove();");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('actions')[0].style.display=\"none\"; " +
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ym-container')[0].style.display=\"none\"; " +
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('cr-area')[0].style.display=\"none\"; " +
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('ad animatable')[0].style.display=\"none\"; " +
                        "})()");

                view.loadUrl("javascript:(function() { " +
                        "document.getElementsByClassName('cta cta-report')[0].style.display=\"none\"; " +
                        "})()");

            }
        });

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (myWebView.canGoBack()) {
                        myWebView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
