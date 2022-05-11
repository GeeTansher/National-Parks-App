package com.example.nationalparksmap.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class util {
    public static final String PARKS_URL = "https://developer.nps.gov/api/v1/parks?StateCode=AZ&api_key=iGL3XJZH5aX2KWrXk8G5wutY9B8ZQZgvOCaoeMhd";

    public static String getParksUrl(String stateCode){
        return "https://developer.nps.gov/api/v1/parks?StateCode=" +
                stateCode +"&api_key=iGL3XJZH5aX2KWrXk8G5wutY9B8ZQZgvOCaoeMhd";
    }

    public static void hideSoftKeyboard(View view){
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
