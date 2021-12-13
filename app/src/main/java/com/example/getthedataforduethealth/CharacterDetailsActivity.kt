package com.example.getthedataforduethealth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.Fade
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.getthedataforduethealth.objects.AppConstants
import com.example.getthedataforduethealth.character.*
import com.example.getthedataforduethealth.databinding.ActivityCharacterDetailsBinding

//TODO:Finish Text Edit, add edittext focusable to remove focus after button pressed
class CharacterDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCharacterDetailsBinding
    private lateinit var characterDetailsViewModel: CharacterViewModel
    private lateinit var characterDao: CharacterDao
    private val inputMethodManager: InputMethodManager by lazy {
        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
    private val goLeft: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.go_left) }
    private val goRight: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.go_right) }
    private var clicked = false
    private var characterId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_character_details
        )
        characterDao = CharacterDatabase.getInstance(application).characterDao
        val repository = CharacterRepository(characterDao)
        val factory = CharacterViewModelFactory(repository)
        characterDetailsViewModel = ViewModelProvider(this, factory)[CharacterViewModel::class.java]
        binding.characterDetailsViewModel = characterDetailsViewModel
        binding.lifecycleOwner = this
        //postponeEnterTransition()
        title = "Details Activity"
        val fade = Fade()
        window.enterTransition = fade
        window.exitTransition = fade
        getCharacterFromDatabase()
        binding.floatingActionButtonGiveNickname.setOnClickListener {
            showEditText()
        }
    }

    private fun getCharacterFromDatabase() {
        val extras = intent.extras ?: return
        characterDetailsViewModel
            .getCharacterFromDatabase(
                extras.getInt(AppConstants.CHARACTER_ID)
            ).observe(this) {
                binding.apply {
                    Glide.with(root).load(it.characterHeadShot)
                        .into(imageViewCharacterDetails)
                    characterName.text = it.characterName
                    if (it.characterNickname != null) {
                        textViewCharacterNickName.visibility = View.VISIBLE
                        textViewCharacterNickName.text = "Nickname: ${it.characterNickname}"
                    }
                    characterHeight.text = "Height: ${it.characterHeight} "
                    characterWeight.text = "Mass: ${it.characterWeight}"
                    characterEyeColor.text = "Eye Color: ${it.characterEyeColor}"
                    characterHairColor.text = "Hair Color: ${it.characterHairColor}"
                    characterGender.text = "Gender: ${it.characterGender}"
                    characterBirthYear.text = "Birth Year: ${it.characterBirthYear}"
                    characterId = it.characterId
                }
            }
    }

    private fun showEditText() {
        clicked = !clicked
        setAnimation(clicked)
        setVisibility(clicked)
        setNickname(clicked)
        unFocusTheEditText(clicked)
    }

    private fun setAnimation(clicked: Boolean) {
        if (clicked) {
            binding.editTextChangeNickName.startAnimation(goLeft)
        } else {
            binding.editTextChangeNickName.startAnimation(goRight)

        }
    }

    private fun unFocusTheEditText(clicked: Boolean) {
        if (!clicked) {
            inputMethodManager.hideSoftInputFromWindow(
                binding.editTextChangeNickName.windowToken,
                0
            )
            binding.editTextChangeNickName.setText("")
        }
    }

    private fun setVisibility(clicked: Boolean) {
        binding.editTextChangeNickName.isVisible = clicked
    }

    private fun setNickname(clicked: Boolean) {
        if (!clicked) {
            Log.d(TAG, "setNickname: ${binding.editTextChangeNickName.text.toString()}")
            characterDetailsViewModel.updateCharacterNickname(
                binding.editTextChangeNickName.text.toString(), characterId
            )
        }
    }
}