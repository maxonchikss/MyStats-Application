package com.example.mystats.utils;

public class Constants {
    public static final String PREFS_NAME = "MyStatsPrefs";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_REST_TIME = "restTime";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_DARK_MODE = "darkMode";

    public static final int DEFAULT_REST_TIME = 90;
    public static final String DEFAULT_LANGUAGE = "ru";

    public static final String[] DAYS_OF_WEEK = {
            "Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс"
    };

    public static final String[] DAYS_OF_WEEK_FULL = {
            "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"
    };

    public static final String[] CATEGORIES = {
            "Фулбади", "Сплит", "Функциональная", "Другое"
    };

    public static final String EXTRA_WORKOUT_ID = "workout_id";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_DAY = "day";
    public static final String EXTRA_REST_TIME = "rest_time";
    public static final String EXTRA_EXERCISE_NAME = "exercise_name";
}