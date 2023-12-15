package org.hamroh.hisob.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.hamroh.hisob.R
import org.hamroh.hisob.ui.about.fragments.MainFragment

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportFragmentManager.beginTransaction().add(R.id.aboutContainer, MainFragment()).commit()
    }
}
