package crabster.rudakov.sberschoollesson17hwk.clientApi;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import crabster.rudakov.sberschoollesson17hwk.Meat;

public abstract class Client {

    public abstract String getContent();

    public abstract String postContent();

    /**
     * Метод конвертирует объект класса 'Meat' в JSON-формат с помощью библиотеки 'Moshi'
     */
    public static String createRequestBody(String title, double price, String category, String description, String image) {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Meat> jsonAdapter = moshi.adapter(Meat.class);
        return jsonAdapter.toJson(new Meat(title, price, category, description, image));
    }

}
