package crabster.rudakov.sberschoollesson17hwk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import crabster.rudakov.sberschoollesson17hwk.clientApi.HttpsURLConnectionApi;
import crabster.rudakov.sberschoollesson17hwk.clientApi.OkHttpApi;

public class MainActivity extends AppCompatActivity {

    private TextView contentView;
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentView = findViewById(R.id.content);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        sendHttpsGetRequest();
        sendHttpsPostRequest();
        sendOkhttpGetRequest();
        sendOkhttpPostRequest();
    }

    /**
     * Метод создаёт OptionsMenu с настройками
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.preferencies_menu, menu);
        return true;
    }

    /**
     * Метод создаёт PreferencesFragment с настройками по нажатию на иконку OptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, new PreferencesFragment())
                    .addToBackStack(null)
                    .commit();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Метод восстанавливает показ ActionBar после закрытия фрагмента с настройками
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Objects.requireNonNull(getSupportActionBar()).show();
    }

    /**
     * Метод по нажатию кнопки отправляет GET-запрос с помощью библиотеки
     * HttpsURLConnection
     */
    private void sendHttpsGetRequest() {
        Button httpsGetButton = findViewById(R.id.https_get_button);
        httpsGetButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new HttpsURLConnectionApi().getContent();
                answerOnButtonClick(content);
            }).start();
        });
    }

    /**
     * Метод по нажатию кнопки отправляет POST-запрос с помощью библиотеки
     * HttpsURLConnection
     */
    private void sendHttpsPostRequest() {
        Button httpsPostButton = findViewById(R.id.https_post_button);
        httpsPostButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new HttpsURLConnectionApi().postContent();
                answerOnButtonClick(content);
            }).start();
        });
    }

    /**
     * Метод по нажатию кнопки отправляет GET-запрос с помощью библиотеки
     * OkHttp, при необходимости кэшируя запрос
     */
    private void sendOkhttpGetRequest() {
        Button okhttpGetButton = findViewById(R.id.okhttp_get_button);
        okhttpGetButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                boolean isCached = getCachingStatusFromPreferences();
                String content = new OkHttpApi(this, isCached).getContent();
                Log.d(Constants.LOG_CACHE_TAG, "" + isCached);
                answerOnButtonClick(content);
            }).start();
        });
    }

    /**
     * Метод по нажатию кнопки отправляет POST-запрос с помощью библиотеки
     * OkHttp, при необходимости кэшируя запрос
     */
    private void sendOkhttpPostRequest() {
        Button okhttpPostButton = findViewById(R.id.okhttp_post_button);
        okhttpPostButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                boolean isCached = getCachingStatusFromPreferences();
                String content = new OkHttpApi(this, isCached).postContent();
                Log.d(Constants.LOG_CACHE_TAG, "" + isCached);
                answerOnButtonClick(content);
            }).start();
        });
    }

    /**
     * Метод предоставляет возможность загрузки полученного по запросу контента
     * в WebView и в TextView, выполняя данные действия в отдельном потоке и
     * сигнализируя о выполнении тост-сообщением
     */
    private void answerOnButtonClick(String content) {
        webView.post(() -> {
            webView.loadDataWithBaseURL(Constants.REQUEST_URL,
                    content,
                    Constants.MIME_TYPE,
                    Constants.ENCODING_TYPE,
                    Constants.REQUEST_URL);
            Toast.makeText(getApplicationContext(), Constants.LOADING_STATUS_DONE, Toast.LENGTH_SHORT).show();
        });
        contentView.post(() -> contentView.setText(content));
    }

    /**
     * Метод получает из Preferences boolean флаг, отмечающий необходимость кэширования
     * запросов
     */
    private boolean getCachingStatusFromPreferences() {
        SharedPreferences dataPreferences = getSharedPreferences(Constants.PREFERENCES_STORAGE_NAME, MODE_PRIVATE);
        return dataPreferences.getBoolean(Constants.CACHE_NEEDED_KEY, false);
    }

}