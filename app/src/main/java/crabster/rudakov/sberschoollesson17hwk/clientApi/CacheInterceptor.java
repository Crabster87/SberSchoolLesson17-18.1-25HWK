package crabster.rudakov.sberschoollesson17hwk.clientApi;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import crabster.rudakov.sberschoollesson17hwk.Constants;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {

    /**
     * Метод создаёт перехватчика для перехвата и обработки запросов до их
     * отправки в код приложения. В нём задаются параметры кэширования и
     * необходимые заголовки
     */
    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        CacheControl cacheControl = new CacheControl.Builder()
                .maxAge(15, TimeUnit.MINUTES)
                .build();

        return response.newBuilder()
                .removeHeader(Constants.CACHE_OLD_HEADER_NAME)
                .removeHeader(Constants.CACHE_NEW_HEADER_NAME)
                .header(Constants.CACHE_NEW_HEADER_NAME, cacheControl.toString())
                .build();
    }

}
