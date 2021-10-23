package crabster.rudakov.sberschoollesson17hwk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MainActivity extends AppCompatActivity {

    private TextView contentView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contentView = findViewById(R.id.content);

        Button okhttpGetButton = findViewById(R.id.okhttp_get_button);
        okhttpGetButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            Disposable disposable = new RxJavaApi().getContent().subscribe((s, throwable) -> {
                if (throwable == null) contentView.setText(s);
                Toast.makeText(getApplicationContext(), Constants.LOADING_STATUS_DONE, Toast.LENGTH_SHORT).show();
            });
            compositeDisposable.add(disposable);
        });

        Button okhttpPostButton = findViewById(R.id.okhttp_post_button);
        okhttpPostButton.setOnClickListener(v -> {
            contentView.setText(Constants.LOADING_STATUS_GOING);
            Disposable disposable = new RxJavaApi().postContent().subscribe((s, throwable) -> {
                if (throwable == null) contentView.setText(s);
                Toast.makeText(getApplicationContext(), Constants.LOADING_STATUS_DONE, Toast.LENGTH_SHORT).show();
            });
            compositeDisposable.add(disposable);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable = null;
    }

}