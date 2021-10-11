package crabster.rudakov.sberschoollesson17hwk;

import android.util.Log;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OkHttpApi extends Client {

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
    private static String getServerResponse(Request request) {
        OkHttpClient httpClient = new OkHttpClient();
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                return body != null ? body.string() : Constants.CONNECT_FAULT;
            } else {
                return Constants.RESPONSE_CODE + response.code();
            }
        } catch (Exception e) {
            Log.e(Constants.TAG, Constants.REQUEST_FAULT, e);
            return e.toString();
        }
    }

}
