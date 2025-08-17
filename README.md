# 🚀 AhamAI - AI-Powered Search App

**AhamAI** is a modern, mobile-optimized AI search application with AMOLED black theme and Sketchware APK conversion support. The app provides intelligent, conversational search experiences using OpenAI-compatible APIs.

## ✨ Key Features

* **🎨 AMOLED Black Theme**: Pure black design optimized for OLED displays
* **🤖 AI Model Selection**: Choose from multiple AI models via dropdown
* **📱 Mobile-First Design**: Optimized for mobile devices and touch interfaces  
* **🎯 Splash Screen**: Pixel-art style AhamAI branding
* **⚡ Real-time Search**: Instant AI-powered responses
* **🔄 Sketchware Compatible**: Ready for APK conversion with dedicated functions

## 🛠️ Technologies Used

* **Frontend**: HTML5, CSS3, JavaScript (ES6+)
* **API**: OpenAI-compatible endpoint (`https://ahamai-api.officialprakashkrsingh.workers.dev`)
* **Styling**: Custom CSS with AMOLED black theme
* **Fonts**: Press Start 2P (pixel-style) for branding
* **Mobile**: Responsive design with touch optimization

## 🚀 Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd ahamai-app
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Run development server**
   ```bash
   npm run dev
   ```

4. **Build for production**
   ```bash
   npm run build
   ```

## 📱 Sketchware APK Conversion

### Required Blocks Code

#### 1. WebView Setup Block
```
WebView: webview1
URL: file:///android_asset/index.html
JavaScript: Enabled
DOM Storage: Enabled
```

#### 2. Initialize App Block
```javascript
// In onCreate event
webview1.evaluateJavascript("sketchwareInit();", null);
```

#### 3. Search Function Block
```javascript
// Create custom function: performSearch(String query)
webview1.evaluateJavascript("sketchwareSearch('" + query + "');", null);
```

#### 4. Get Result Block
```javascript
// Create custom function: getSearchResult()
webview1.evaluateJavascript("sketchwareGetResult();", new ValueCallback<String>() {
    @Override
    public void onReceiveValue(String result) {
        // Handle result
        showMessage(result);
    }
});
```

#### 5. Model Selection Block
```javascript
// Create custom function: setAIModel(String model)
webview1.evaluateJavascript("sketchwareSetModel('" + model + "');", null);
```

#### 6. Get Available Models Block
```javascript
// Create custom function: getModels()
webview1.evaluateJavascript("sketchwareGetModels();", new ValueCallback<String>() {
    @Override
    public void onReceiveValue(String models) {
        // Handle models array
        parseModels(models);
    }
});
```

### Sketchware Project Structure

```
MyApp/
├── assets/
│   ├── index.html          # Main app file
│   ├── style.css           # AMOLED theme styles
│   ├── firebase-config.js  # Firebase configuration
│   ├── developer-info.js   # App info
│   └── ai-training.js      # AI training data
├── java/
│   └── MainActivity.java   # WebView container
└── res/
    ├── layout/
    │   └── activity_main.xml
    └── values/
        └── strings.xml
```

### Available JavaScript Functions

| Function | Description | Usage |
|----------|-------------|-------|
| `sketchwareInit()` | Initialize the app | Call in WebView onPageFinished |
| `sketchwareSearch(query)` | Perform AI search | Pass search string |
| `sketchwareGetResult()` | Get current result | Returns HTML content |
| `sketchwareSetModel(model)` | Set AI model | Pass model name |
| `sketchwareGetModels()` | Get available models | Returns array of models |

### Permissions Required

Add these permissions to your AndroidManifest.xml:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## 🎨 Design Features

### AMOLED Black Theme
- **Background**: Pure black (#000000) for true AMOLED optimization
- **Text**: Pure white (#FFFFFF) for maximum contrast
- **Accents**: Dark grays (#111111, #222222, #333333)
- **Borders**: Subtle gray borders (#333333, #555555)

### Typography
- **Logo**: Press Start 2P (pixel-art style)
- **Body**: System fonts (-apple-system, BlinkMacSystemFont, etc.)
- **Sizes**: Responsive scaling for mobile devices

### Mobile Optimizations
- **Touch-friendly**: 44px minimum touch targets
- **Responsive**: Fluid layout adapting to screen sizes
- **Performance**: Optimized animations and transitions
- **Battery**: AMOLED black reduces power consumption

## 🔧 API Configuration

The app uses the AhamAI API endpoint:
- **Base URL**: `https://ahamai-api.officialprakashkrsingh.workers.dev`
- **API Key**: `ahamaibyprakash25`
- **Compatible with**: OpenAI Chat Completions API format

### Endpoints Used
- `GET /v1/models` - Fetch available AI models
- `POST /v1/chat/completions` - Send chat requests

## 📁 Project Structure

```
ahamai-app/
├── index.html              # Main application file
├── style.css              # AMOLED theme styles  
├── package.json           # Project dependencies
├── README.md              # This documentation
├── firebase-config.js     # Firebase integration
├── developer-info.js      # App information
├── ai-training.js         # AI training data
└── assets/                # Additional resources
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For support and questions:
- Create an issue in the repository
- Check the Sketchware blocks documentation above
- Review the JavaScript functions reference

---

**AhamAI** - Bringing AI-powered search to mobile with style 🚀
