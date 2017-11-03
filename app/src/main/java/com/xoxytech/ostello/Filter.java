package com.xoxytech.ostello;

import android.app.Activity;
import android.os.Bundle;

public class Filter extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.filtercloseanim, R.anim.filtercloseanim);
    }
}
