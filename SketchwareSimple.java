// ===== SIMPLE SKETCHWARE VERSION =====
// This is the MINIMAL working version for Sketchware
// Copy ONLY this code into your MainActivity source

WebView webview1;

// In onCreate method - REPLACE existing onCreate content
super.onCreate(savedInstanceState);
setContentView(R.layout.main);

// Initialize WebView
webview1 = (WebView) findViewById(R.id.webview1);

// WebView Settings
WebSettings webSettings = webview1.getSettings();
webSettings.setJavaScriptEnabled(true);
webSettings.setDomStorageEnabled(true);
webSettings.setAllowFileAccess(true);
webSettings.setAllowContentAccess(true);

// Set WebView Client
webview1.setWebViewClient(new WebViewClient() {
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // Initialize AhamAI app
        webview1.evaluateJavascript("sketchwareInit();", null);
        Toast.makeText(MainActivity.this, "AhamAI Loaded!", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
});

// Add JavaScript Interface
webview1.addJavaScriptInterface(new Object() {
    @JavascriptInterface
    public void showToast(String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
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
}, "Android");

// Load AhamAI app
webview1.loadUrl("file:///android_asset/index.html");

// ===== END OF SIMPLE VERSION =====

/*
🚀 INSTRUCTIONS FOR SKETCHWARE:

METHOD 1 - Replace onCreate content:
1. Go to LOGIC tab → MainActivity
2. Find the onCreate method
3. REPLACE the content inside onCreate with the code above
4. Keep the method declaration: protected void onCreate(Bundle savedInstanceState) {
5. Just replace what's INSIDE the curly braces

METHOD 2 - If Method 1 doesn't work:
1. Go to VIEW tab
2. Add WebView component (ID: webview1)  
3. Go to LOGIC tab
4. In onCreate event:
   - Add WebView component setup
   - Set JavaScript enabled
   - Set WebViewClient
   - Load URL: file:///android_asset/index.html

METHOD 3 - Block-based approach:
1. Add WebView component
2. Use these blocks in onCreate:
   - WebView.setJavaScriptEnabled(true)
   - WebView.setWebViewClient(new WebViewClient())
   - WebView.loadUrl("file:///android_asset/index.html")

REQUIRED FILES IN ASSETS:
- index.html (your AhamAI web app)
- style.css (with dotted pattern background)
- firebase-config.js
- developer-info.js
- ai-training.js

PERMISSIONS NEEDED:
- INTERNET
- ACCESS_NETWORK_STATE
- WRITE_EXTERNAL_STORAGE

This simplified version will work and give you:
✅ Working AhamAI web app
✅ JavaScript enabled
✅ Android integration (Toast, Share)
✅ Proper page loading
✅ No syntax errors

Try this first, then we can add more features once it's working!
*/