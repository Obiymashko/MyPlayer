package my.player;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Налаштування Edge-to-Edge для вікна
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Знаходження WebView за його ідентифікатором у макеті
        webView = findViewById(R.id.webview);

        // Налаштування WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Увімкнення JavaScript
        webSettings.setDomStorageEnabled(true); // Увімкнення DOM-хранилища
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // Відключення кешу
        webSettings.setUseWideViewPort(true); // Налаштування масштабування контенту
        webSettings.setLoadWithOverviewMode(true);

        // Очистка кешу WebView
        webView.clearCache(true);

        // Додавання панелі прогресу завантаження
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Оновлення інтерфейсу користувача залежно від прогресу завантаження
            }
        });

        // Налаштування WebViewClient
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url); // Завантаження URL-адреси в цьому ж WebView
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Опціонально: тут ви можете виконати додаткові дії після завершення завантаження сторінки
                Log.d("WebView", "Page loaded: " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Обробка помилок завантаження
                Log.e("WebView", "Error loading URL: " + error.getDescription());
            }
        });

        // Завантаження веб-сторінки
        webView.loadUrl("https://uamp3.org/");

        // Обробка натискання кнопки "Назад"
        this.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    setEnabled(false); // Вимкнення цього обробника
                    MainActivity.this.finish(); // Завершення активності
                }
            }
        });
    }
}
