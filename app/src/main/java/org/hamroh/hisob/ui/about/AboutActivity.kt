package org.hamroh.hisob.ui.About;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.hamroh.hisob.R;
import org.hamroh.hisob.ui.About.Fragments.MainFragment;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportFragmentManager().beginTransaction().add(R.id.aboutContainer, new MainFragment()).commit();

    }
}
