package crabster.rudakov.sberschoollesson17hwk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

        Button httpsGetButton = findViewById(R.id.https_get_button);
        httpsGetButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new HttpsURLConnectionApi().getContent();
                answerOnButtonClick(content);
            }).start();
        });

        Button httpsPostButton = findViewById(R.id.https_post_button);
        httpsPostButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new HttpsURLConnectionApi().postContent();
                answerOnButtonClick(content);
            }).start();
        });

        Button okhttpGetButton = findViewById(R.id.okhttp_get_button);
        okhttpGetButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new OkHttpApi().getContent();
                answerOnButtonClick(content);
            }).start();
        });

        Button okhttpPostButton = findViewById(R.id.okhttp_post_button);
        okhttpPostButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            new Thread(() -> {
                String content = new OkHttpApi().postContent();
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

}