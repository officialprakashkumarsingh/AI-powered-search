# 📱 AhamAI Sketchware Blocks Code

This file contains all the necessary blocks code to convert AhamAI web app into an Android APK using Sketchware.

## 🚀 Project Setup

### 1. Create New Project
- **Project Name**: AhamAI
- **Package Name**: com.ahamai.app
- **App Name**: AhamAI

### 2. Add Permissions
Add these permissions in AndroidManifest.xml:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## 🎨 Layout Setup

### activity_main.xml
```xml
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
```

## 📝 Java Code Blocks

### MainActivity.java - onCreate Method
```java
// WebView Setup Block
WebView webview1 = findViewById(R.id.webview1);
WebSettings webSettings = webview1.getSettings();
webSettings.setJavaScriptEnabled(true);
webSettings.setDomStorageEnabled(true);
webSettings.setAllowFileAccess(true);
webSettings.setAllowContentAccess(true);
webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

// Add JavaScript Interface
webview1.addJavaScriptInterface(new WebAppInterface(this), "Android");

// Set WebView Client
webview1.setWebViewClient(new WebViewClient() {
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // Initialize AhamAI app
        webview1.evaluateJavascript("sketchwareInit();", null);
    }
    
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }
});

// Load the app
webview1.loadUrl("file:///android_asset/index.html");
```

### WebAppInterface Class
```java
public class WebAppInterface {
    Context mContext;

    WebAppInterface(Context c) {
        mContext = c;
    }

    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    
    @JavascriptInterface
    public void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");
        
        Intent shareIntent = Intent.createChooser(sendIntent, "Share via");
        mContext.startActivity(shareIntent);
    }
}
```

## 🔧 Custom Functions

### Function: performSearch
```java
public void performSearch(String query) {
    webview1.evaluateJavascript("sketchwareSearch('" + query + "');", null);
}
```

### Function: getSearchResult
```java
public void getSearchResult() {
    webview1.evaluateJavascript("sketchwareGetResult();", new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String result) {
            // Handle the result
            if (result != null && !result.equals("null")) {
                showMessage("Result: " + result);
            }
        }
    });
}
```

### Function: setAIModel
```java
public void setAIModel(String model) {
    webview1.evaluateJavascript("sketchwareSetModel('" + model + "');", null);
}
```

### Function: getAvailableModels
```java
public void getAvailableModels() {
    webview1.evaluateJavascript("sketchwareGetModels();", new ValueCallback<String>() {
        @Override
        public void onReceiveValue(String models) {
            // Handle models array
            if (models != null && !models.equals("null")) {
                parseModels(models);
            }
        }
    });
}
```

### Function: parseModels
```java
public void parseModels(String modelsJson) {
    try {
        // Remove quotes from JSON string
        modelsJson = modelsJson.replace("\"", "");
        modelsJson = modelsJson.replace("[", "");
        modelsJson = modelsJson.replace("]", "");
        
        String[] models = modelsJson.split(",");
        
        // Show models in a dialog or list
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Available AI Models");
        builder.setItems(models, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAIModel(models[which].trim());
                showMessage("Model set to: " + models[which].trim());
            }
        });
        builder.show();
        
    } catch (Exception e) {
        showMessage("Error parsing models: " + e.getMessage());
    }
}
```

### Function: showMessage
```java
public void showMessage(String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
}
```

## 🎯 Event Handlers

### Back Button Handler
```java
@Override
public void onBackPressed() {
    if (webview1.canGoBack()) {
        webview1.goBack();
    } else {
        super.onBackPressed();
    }
}
```

### Menu Options
```java
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.refresh:
            webview1.reload();
            return true;
        case R.id.models:
            getAvailableModels();
            return true;
        case R.id.share:
            getSearchResult();
            return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}
```

## 📁 Assets Folder Structure
```
assets/
├── index.html              # Main AhamAI app file
├── style.css              # AMOLED theme styles
├── firebase-config.js      # Firebase configuration
├── developer-info.js       # App information
└── ai-training.js          # AI training data
```

## 🎨 Menu Resource (res/menu/main_menu.xml)
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android">
    <item
        android:id="@+id/refresh"
        android:title="Refresh"
        android:icon="@android:drawable/ic_menu_rotate" />
    <item
        android:id="@+id/models"
        android:title="AI Models"
        android:icon="@android:drawable/ic_menu_preferences" />
    <item
        android:id="@+id/share"
        android:title="Share Result"
        android:icon="@android:drawable/ic_menu_share" />
</menu>
```

## 🔄 JavaScript Bridge Functions

These functions are already available in the web app and can be called from Android:

### Available JavaScript Functions
| Function | Purpose | Usage |
|----------|---------|-------|
| `sketchwareInit()` | Initialize the app | Call in onPageFinished |
| `sketchwareSearch(query)` | Perform search | Pass search string |
| `sketchwareGetResult()` | Get current result | Returns HTML content |
| `sketchwareSetModel(model)` | Set AI model | Pass model name |
| `sketchwareGetModels()` | Get available models | Returns array of models |

## 🚀 Build Instructions

1. **Copy Assets**: Place all web files (index.html, style.css, etc.) in the `assets` folder
2. **Add Permissions**: Update AndroidManifest.xml with required permissions
3. **Implement Java Code**: Add all the Java code blocks to MainActivity.java
4. **Create Menu**: Add the menu resource file
5. **Test**: Build and test the APK on a device

## 🎯 Pro Tips

### Performance Optimization
```java
// Add these settings for better performance
webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
webSettings.setAppCacheEnabled(true);
webSettings.setDatabaseEnabled(true);
```

### Network Security
```java
// Handle network security config
webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
```

### Error Handling
```java
webview1.setWebChromeClient(new WebChromeClient() {
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d("WebView", consoleMessage.message());
        return true;
    }
});
```

## 🎉 Final Notes

- Make sure all assets are properly copied to the assets folder
- Test the app on different Android versions
- The app will work offline once loaded initially
- All animations and AMOLED theme will be preserved
- The app supports both portrait and landscape modes

---

**Happy Coding! 🚀** Your AhamAI app is ready for the Play Store!