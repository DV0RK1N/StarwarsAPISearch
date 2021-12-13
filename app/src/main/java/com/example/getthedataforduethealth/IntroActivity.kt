package com.example.getthedataforduethealth

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.getthedataforduethealth.objects.AppConstants
import com.example.getthedataforduethealth.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    lateinit var sharedPreferences: SharedPreferences
    var layoutBigPicture = true
    var pathChosen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE)
        pathChosen = sharedPreferences.getBoolean(AppConstants.LAYOUT_CHOSEN, false)
        if (pathChosen) {
            openMainActivity()
            finish()
        }
        binding.apply {
            chooseLayoutOption.setOnClickListener {
                chooseLayout.isVisible = !chooseLayout.isVisible
            }
            val editor = sharedPreferences.edit()
                chooseBigPicture.setOnClickListener {
                    layoutBigPicture = true
                    pathChosen = true
                    editor.putBoolean(AppConstants.LAYOUT_CHOICE, layoutBigPicture)
                    editor.putBoolean(AppConstants.LAYOUT_CHOSEN, pathChosen)
                    editor.apply()
                    openMainActivity()
                }
                chooseSmallPicture.setOnClickListener {
                    layoutBigPicture = false
                    pathChosen = true
                    editor.putBoolean(AppConstants.LAYOUT_CHOICE, layoutBigPicture)
                    editor.putBoolean(AppConstants.LAYOUT_CHOSEN, pathChosen)
                    editor.apply()
                    openMainActivity()
                }

        }
    }

    private fun openMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}