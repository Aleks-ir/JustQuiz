package tisserand.alexey.justquiz.activity;

import android.os.Bundle;

import tisserand.alexey.justquiz.R;
import tisserand.alexey.justquiz.utilities.ActionBar;

public class AboutAppActivity extends ActionBar {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        initToolbar();

    }
}
