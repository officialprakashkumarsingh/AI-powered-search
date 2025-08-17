package com.ahamai.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    
    private WebView webview1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize WebView
        webview1 = findViewById(R.id.webview1);
        setupWebView();
        
        // Load AhamAI app
        webview1.loadUrl("file:///android_asset/index.html");
    }
    
    private void setupWebView() {
        // WebView Settings
        WebSettings webSettings = webview1.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        // Performance optimizations
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        
        // Add JavaScript Interface
        webview1.addJavaScriptInterface(new WebAppInterface(this), "Android");
        
        // Set WebView Client
        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Initialize AhamAI app
                webview1.evaluateJavascript("sketchwareInit();", null);
                showMessage("AhamAI Loaded Successfully!");
            }
            
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                showMessage("Error loading page: " + description);
            }
        });
        
        // Set WebChrome Client for console messages and debugging
        webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("AhamAI_WebView", consoleMessage.message() + " -- From line " 
                     + consoleMessage.lineNumber() + " of " + consoleMessage.sourceId());
                return true;
            }
            
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    // Page fully loaded
                    Log.d("AhamAI", "Page fully loaded");
                }
            }
        });
    }
    
    // JavaScript Interface Class
    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showToast(String toast) {
            runOnUiThread(() -> {
                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            });
        }
        
        @JavascriptInterface
        public void shareText(String text) {
            runOnUiThread(() -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                
                Intent shareIntent = Intent.createChooser(sendIntent, "Share AhamAI Result");
                startActivity(shareIntent);
            });
        }
        
        @JavascriptInterface
        public void copyToClipboard(String text) {
            runOnUiThread(() -> {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("AhamAI Result", text);
                clipboard.setPrimaryClip(clip);
                showMessage("Copied to clipboard!");
            });
        }
        
        @JavascriptInterface
        public void vibrate(int duration) {
            runOnUiThread(() -> {
                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(duration);
                }
            });
        }
    }
    
    // Custom Functions for AhamAI
    
    public void performSearch(String query) {
        if (query != null && !query.trim().isEmpty()) {
            webview1.evaluateJavascript("sketchwareSearch('" + query.replace("'", "\\'") + "');", null);
            showMessage("Searching: " + query);
        } else {
            showMessage("Please enter a search query");
        }
    }
    
    public void getSearchResult() {
        webview1.evaluateJavascript("sketchwareGetResult();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String result) {
                if (result != null && !result.equals("null") && !result.equals("\"\"")) {
                    // Remove quotes from result
                    result = result.replace("\"", "");
                    if (result.length() > 100) {
                        result = result.substring(0, 100) + "...";
                    }
                    showLongMessage("Latest Result: " + result);
                } else {
                    showMessage("No result available. Try searching first!");
                }
            }
        });
    }
    
    public void setAIModel(String model) {
        if (model != null && !model.trim().isEmpty()) {
            webview1.evaluateJavascript("sketchwareSetModel('" + model + "');", null);
            showMessage("AI Model set to: " + model);
        }
    }
    
    public void getAvailableModels() {
        webview1.evaluateJavascript("sketchwareGetModels();", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String models) {
                if (models != null && !models.equals("null")) {
                    parseModels(models);
                } else {
                    showMessage("Unable to fetch models. Check your connection.");
                }
            }
        });
    }
    
    public void parseModels(String modelsJson) {
        try {
            // Clean up JSON string
            modelsJson = modelsJson.replace("\"", "");
            modelsJson = modelsJson.replace("[", "");
            modelsJson = modelsJson.replace("]", "");
            
            if (modelsJson.trim().isEmpty()) {
                showMessage("No models available");
                return;
            }
            
            String[] models = modelsJson.split(",");
            
            // Show models in a dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("🤖 Choose AI Model");
            builder.setItems(models, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String selectedModel = models[which].trim();
                    setAIModel(selectedModel);
                }
            });
            
            builder.setNegativeButton("Cancel", null);
            builder.show();
            
        } catch (Exception e) {
            Log.e("AhamAI", "Error parsing models: " + e.getMessage());
            showMessage("Error loading models: " + e.getMessage());
        }
    }
    
    public void refreshApp() {
        webview1.reload();
        showMessage("Refreshing AhamAI...");
    }
    
    public void clearCache() {
        webview1.clearCache(true);
        webview1.clearHistory();
        showMessage("Cache cleared!");
    }
    
    // Utility Methods
    
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    public void showLongMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    
    // Menu Methods
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                refreshApp();
                return true;
            case R.id.models:
                getAvailableModels();
                return true;
            case R.id.share_result:
                getSearchResult();
                return true;
            case R.id.clear_cache:
                clearCache();
                return true;
            case R.id.search_custom:
                showCustomSearchDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void showCustomSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🔍 Custom Search");
        
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter your search query...");
        builder.setView(input);
        
        builder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String query = input.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                }
            }
        });
        
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
    
    // Activity Lifecycle Methods
    
    @Override
    public void onBackPressed() {
        if (webview1.canGoBack()) {
            webview1.goBack();
        } else {
            // Show exit confirmation
            new AlertDialog.Builder(this)
                .setTitle("Exit AhamAI")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        webview1.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        webview1.onPause();
    }
    
    @Override
    protected void onDestroy() {
        if (webview1 != null) {
            webview1.destroy();
        }
        super.onDestroy();
    }
}

/*
=== ADDITIONAL FILES NEEDED ===

1. res/layout/activity_main.xml:
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <WebView
        android:id="@+id/webview1"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>

2. res/menu/main_menu.xml:
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/search_custom"
        android:title="Custom Search"
        android:icon="@android:drawable/ic_menu_search" />
    <item
        android:id="@+id/models"
        android:title="AI Models"
        android:icon="@android:drawable/ic_menu_preferences" />
    <item
        android:id="@+id/share_result"
        android:title="Share Result"
        android:icon="@android:drawable/ic_menu_share" />
    <item
        android:id="@+id/refresh"
        android:title="Refresh"
        android:icon="@android:drawable/ic_menu_rotate" />
    <item
        android:id="@+id/clear_cache"
        android:title="Clear Cache"
        android:icon="@android:drawable/ic_menu_delete" />
</menu>

3. AndroidManifest.xml permissions:
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.VIBRATE" />

4. Assets folder: Copy all web files (index.html, style.css, etc.) to assets/

=== USAGE INSTRUCTIONS ===
1. Copy this entire MainActivity.java code to your Sketchware project
2. Create the layout and menu XML files as shown above
3. Add the permissions to AndroidManifest.xml
4. Copy all AhamAI web files to the assets folder
5. Build and run your APK!

=== FEATURES INCLUDED ===
- Complete WebView setup with optimizations
- JavaScript bridge for app interaction
- Custom search dialog
- AI model selection
- Result sharing and copying
- Cache management
- Exit confirmation
- Error handling and logging
- Performance optimizations
- Mobile-friendly interface

Your AhamAI app is ready for the Play Store! 🚀
*/