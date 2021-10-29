package crabster.rudakov.sberschoollesson17hwk;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class PreferencesFragment extends PreferenceFragmentCompat {

    /**
     * Метод помимо основного функционала скрывает ActionBar для фрагмента
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();
    }

    /**
     * Метод помимо основного функционала устанавливает фоновый цвет
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setBackgroundColor(Color.MAGENTA);
    }

    /**
     * Метод создаёт Preferences из XML-layout
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferencies);
    }

    /**
     * Метод реагирует на изменение настроек пользователем и производит необходимые
     * действия согласно логике
     */
    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());

        reactThemeCheckBoxPreference(prefs);
        reactCacheSwitchPreference(prefs);
        reactColorListPreferences();
        return super.onPreferenceTreeClick(preference);
    }

    /**
     * Метод изменяет тему приложения по нажатию CheckBox
     */
    private void reactThemeCheckBoxPreference(SharedPreferences prefs) {
        boolean isDarkThemeChosen = prefs.getBoolean(Constants.PREFERENCES_THEME_KEY, false);
        if (isDarkThemeChosen)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    /**
     * Метод устанавляивает значение флага "кэшировать/не кэшировать" и сохраняет его
     * в Preferences для дальнейшего использования в Activity
     */
    private void reactCacheSwitchPreference(SharedPreferences prefs) {
        boolean isCached = prefs.getBoolean(Constants.PREFERENCES_CACHE_KEY, false);
        SharedPreferences dataPreferences = requireActivity().getSharedPreferences(Constants.PREFERENCES_STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataPreferences.edit();
        editor.putBoolean(Constants.CACHE_NEEDED_KEY, isCached).apply();
    }

    /**
     * Метод устанавляивает цвет фона при выборе элемента списка
     */
    private void reactColorListPreferences() {
        ListPreference listPreference = getPreferenceManager().findPreference(Constants.PREFERENCES_COLOR_KEY);
        Objects.requireNonNull(listPreference).setOnPreferenceChangeListener((preference, newValue) -> {
            int index = listPreference.findIndexOfValue(newValue.toString());
            switch (index) {
                case 0:
                    getListView().setBackgroundColor(Color.DKGRAY);
                    break;
                case 1:
                    getListView().setBackgroundColor(Color.RED);
                    break;
                case 2:
                    getListView().setBackgroundColor(Color.GREEN);
                    break;
                case 3:
                    getListView().setBackgroundColor(Color.MAGENTA);
                    break;
            }
            return true;
        });
    }

}
