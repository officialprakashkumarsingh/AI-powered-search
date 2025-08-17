// ===== SKETCHWARE COMPATIBLE CODE =====
// Copy ONLY the code below (without package and imports)
// Paste this into your MainActivity source editor in Sketchware

private WebView webview1;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @JavascriptInterface
    public void shareText(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                sendIntent.setType("text/plain");
                
                Intent shareIntent = Intent.createChooser(sendIntent, "Share AhamAI Result");
                startActivity(shareIntent);
            }
        });
    }
    
    @JavascriptInterface
    public void copyToClipboard(String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("AhamAI Result", text);
                clipboard.setPrimaryClip(clip);
                showMessage("Copied to clipboard!");
            }
        });
    }
    
    @JavascriptInterface
    public void vibrate(int duration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.os.Vibrator vibrator = (android.os.Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(duration);
                }
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

// ===== END OF SKETCHWARE CODE =====

/*
📱 STEP-BY-STEP INSTRUCTIONS FOR SKETCHWARE:

1. CREATE PROJECT:
   - Project Name: AhamAI
   - Package: com.semrush.app (keep your existing one)

2. ADD WEBVIEW:
   - Go to VIEW tab
   - Add WebView component
   - Set ID to: webview1

3. ADD PERMISSIONS:
   - Go to PERMISSION tab
   - Enable: INTERNET, ACCESS_NETWORK_STATE, WRITE_EXTERNAL_STORAGE, VIBRATE

4. ADD MENU (Optional):
   - Go to RESOURCE tab
   - Add menu resource: main_menu.xml
   - Copy the menu XML from previous instructions

5. ADD WEB FILES:
   - Go to ASSET tab
   - Add: index.html, style.css, firebase-config.js, developer-info.js, ai-training.js

6. ADD JAVA CODE:
   - Go to LOGIC tab
   - Click MainActivity
   - Click Source button
   - REPLACE ALL EXISTING CODE with the code above
   - Make sure to copy from "private WebView webview1;" to the end

7. BUILD APK:
   - Click Run/Build
   - Test on device

🔧 TROUBLESHOOTING:
- If R.layout.main error: Change to R.layout.activity_main
- If menu errors: Remove menu-related code or add menu XML
- If WebView not found: Make sure WebView ID is exactly "webview1"

Your AhamAI app will be ready! 🚀
*/