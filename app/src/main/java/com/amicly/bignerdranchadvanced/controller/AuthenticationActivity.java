package com.amicly.bignerdranchadvanced.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.amicly.bignerdranchadvanced.DataManager;
import com.amicly.bignerdranchadvanced.helper.FoursquareOauthUriHelper;
import com.amicly.bignerdranchadvanced.model.TokenStore;

/**
 * Created by daz on 8/30/16.
 */

public class AuthenticationActivity extends AppCompatActivity {

    private WebView mWebView;
    private DataManager mDataManager;

    public static Intent newIntent(Context context) {
        return new Intent(context, AuthenticationActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWebView = new WebView(this);
        setContentView(mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(mWebViewClient);

        mDataManager = DataManager.get(this);
        mWebView.loadUrl(mDataManager.getAuthenticationUrl());
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(url.contains(DataManager.OAUTH_REDIRECT_URI)) {
                FoursquareOauthUriHelper uriHelper =
                        new FoursquareOauthUriHelper(url);
                if(uriHelper.isAuthorized()) {

                    String accessToken = uriHelper.getAccessToken();
                    TokenStore tokenStore =
                            TokenStore.get(AuthenticationActivity.this);
                    tokenStore.setAccessToken(accessToken);
                }
                finish();
            }
            return false;
        }
    };
}