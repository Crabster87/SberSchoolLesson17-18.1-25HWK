package crabster.rudakov.sberschoollesson17hwk.clientApi;

import android.content.Context;
import android.util.Log;

import java.io.File;

import crabster.rudakov.sberschoollesson17hwk.Constants;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpApi extends Client {

    private final Context context;
    private final boolean isCached;

    public OkHttpApi(Context context, boolean isCached) {
        this.context = context;
        this.isCached = isCached;
    }

    /**
     * Метод задаёт параметры для отправки запроса типа 'GET' по протоколу HTTPS
     * с помощью библиотеки 'OkHttp'
     */
    @Override
    public String getContent() {
        Request request = new Request.Builder()
                .url(HttpUrl.get(Constants.REQUEST_URL))
                .addHeader(Constants.REQUEST_HEADER_NAME, Constants.REQUEST_HEADER_VALUE)
                .build();

        return getServerResponse(request);
    }

    /**
     * Метод задаёт параметры для отправки запроса типа 'POST' по протоколу HTTPS
     * с помощью библиотеки 'OkHttp', в тело запроса помещается созданный с
     * необходимыми параметрами объект, который конвертируется в JSON формат
     */
    @Override
    public String postContent() {
        Request request = new Request.Builder()
                .url(HttpUrl.get(Constants.REQUEST_URL))
                .addHeader(Constants.REQUEST_HEADER_NAME, Constants.REQUEST_HEADER_VALUE)
                .addHeader(Constants.REQUEST_PROPERTY_KEY, Constants.REQUEST_PROPERTY_VALUE)
                .post(RequestBody.create(createRequestBody("Fresh Meat",
                        1200,
                        "Beef",
                        "Beef steaks",
                        "steak.image.com").getBytes()))
                .build();

        return getServerResponse(request);
    }

    /**
     * Метод устанавливает соединение и обрабатывает полученный от сервера ответ:
     * в случае положительного ответа возвращаются строковые представления даннных,
     * в любом другом случае - полученный код состояния. Обрабатываются все
     * возможные исключения и закрываются потоки и соединения
     */
    private String getServerResponse(Request request) {
        OkHttpClient httpClient = createOkHttpClient(isCached);

        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body != null ? body.string() : Constants.CONNECT_FAULT;
            } else {
                return Constants.RESPONSE_CODE + response.code();
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_EXCEPTION_TAG, Constants.REQUEST_FAULT, e);
            return e.toString();
        }
    }

    /**
     * Метод создаёт клиента с опцией кэшировнаия или её отсутствием(в зависимости
     * от переданного флага)
     */
    private OkHttpClient createOkHttpClient(boolean isCachedFlag) {
        OkHttpClient httpClient;
        if (!isCachedFlag) {
            httpClient = new OkHttpClient();
        } else {
            File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            httpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new CacheInterceptor())
                    .cache(cache)
                    .build();
        }
        return httpClient;
    }

}
