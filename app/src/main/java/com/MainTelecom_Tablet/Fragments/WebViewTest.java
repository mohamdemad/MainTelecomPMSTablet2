package com.MainTelecom_Tablet.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.MainTelecom_Tablet.R;

/**
 * Created by MOHAMED on 22/05/2016.
 */
public class WebViewTest extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View web = inflater.inflate(R.layout.webpage, container, false);

        WebView webView = (WebView) web.findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl("http://197.45.55.244/Android/ticket.php");
        webView.loadUrl("https://www.google.com.eg/webhp?source=search_app");

        return web;

    }
}
