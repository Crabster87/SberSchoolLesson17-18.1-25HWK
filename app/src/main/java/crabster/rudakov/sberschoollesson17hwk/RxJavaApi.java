package crabster.rudakov.sberschoollesson17hwk;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaApi {

    private final OkHttpApi okHttpApi = new OkHttpApi();

    /**
     * Метод создаёт подписчика, следящего за получением 'GET' запроса по
     * протоколу HTTPS с помощью библиотеки 'OkHttp'
     */
    public Single<String> getContent() {
        Single<String> observerGet = Single.fromCallable(okHttpApi::getContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observerGet;
    }

     /**
     * Метод создаёт подписчика, следящего за получением 'POST' запроса по
     * протоколу HTTPS с помощью библиотеки 'OkHttp'
     */
    public Single<String> postContent() {
        Single<String> observerPost = Single.fromCallable(okHttpApi::postContent)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observerPost;
    }

}
