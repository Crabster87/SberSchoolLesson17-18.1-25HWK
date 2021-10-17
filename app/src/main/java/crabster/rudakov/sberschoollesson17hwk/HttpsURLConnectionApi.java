package crabster.rudakov.sberschoollesson17hwk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpsURLConnectionApi extends Client {

    /**
     * Метод задаёт параметры для отправки запроса типа 'GET' по протоколу HTTPS.
     * Обрабатываются все возможные исключения и закрываются потоки и соединения
     */
    @Override
    public String getContent() {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(Constants.REQUEST_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(Constants.REQUEST_TYPE_GET);
            connection.setConnectTimeout(Constants.CONNECT_TIMEOUT_DURATION);
            connection.setReadTimeout(Constants.READ_TIMEOUT_DURATION);

            return getServerResponse(connection);
        } catch (Exception e) {
            Log.e(Constants.TAG, Constants.REQUEST_FAULT, e);
            return e.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Метод задаёт параметры для отправки запроса типа 'POST' по протоколу HTTPS,
     * в тело запроса помещается созданный с необходимыми параметрами объект,
     * который конвертируется в JSON формат. Обрабатываются все возможные исключения
     * и закрываются потоки и соединения
     */
    @Override
    public String postContent() {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(Constants.REQUEST_URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(Constants.REQUEST_TYPE_POST);
            connection.setConnectTimeout(Constants.CONNECT_TIMEOUT_DURATION);
            connection.setReadTimeout(Constants.READ_TIMEOUT_DURATION);
            connection.addRequestProperty(Constants.REQUEST_PROPERTY_KEY,
                                          Constants.REQUEST_PROPERTY_VALUE);
            connection.setDoOutput(true);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(createRequestBody("Fresh Meat",
                                          1200,
                                       "Beef",
                                     "Beef steaks",
                                        "steak.image.com"));
            writer.flush();

            return getServerResponse(connection);
        } catch (Exception e) {
            Log.e(Constants.TAG, Constants.REQUEST_FAULT, e);
            return e.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Метод устанавливает соединение и обрабатывает полученный от сервера ответ
     * в виде кода состояния HTTP: в случае ответа в виде 200(OK) и 201(CREATED)
     * возвращаются строковые представления даннных, в любом другом случае -
     * полученный код состояния
     */
    private String getServerResponse(HttpsURLConnection connection) throws IOException {
        connection.connect();
        int responseCode = connection.getResponseCode();
        if (responseCode != Constants.RESPONSE_CODE_OK && responseCode != Constants.RESPONSE_CODE_CREATED) {
            return Constants.RESPONSE_CODE_TEXT + responseCode;
        } else {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            return builder.toString();
        }
    }

}
