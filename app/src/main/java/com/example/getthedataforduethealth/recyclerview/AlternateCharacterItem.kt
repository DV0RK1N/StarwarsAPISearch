package com.example.getthedataforduethealth.recyclerview

import android.view.View
import com.bumptech.glide.Glide
import com.example.getthedataforduethealth.R
import com.example.getthedataforduethealth.character.CharacterIntroduction
import com.example.getthedataforduethealth.databinding.ActivityCharacterDetailsBinding
import com.example.getthedataforduethealth.databinding.CharacterItemAlternateLayoutBinding
import com.xwray.groupie.viewbinding.BindableItem

class AlternateCharacterItem(val characterIntroduction: CharacterIntroduction) :
    BindableItem<CharacterItemAlternateLayoutBinding>() {

    override fun bind(viewBinding: CharacterItemAlternateLayoutBinding, position: Int) {
        Glide.with(viewBinding.root).load(characterIntroduction.characterHeadShot)
            .into(viewBinding.imageViewCharacterPictureAlternate)
        if (characterIntroduction.characterNickname != null)
            viewBinding.textViewCharacterNameAlternate.text =
                characterIntroduction.characterNickname
        else
            viewBinding.textViewCharacterNickNameAlternate.visibility = View.GONE
        viewBinding.textViewCharacterNameAlternate.text = characterIntroduction.characterName
    }

    override fun getLayout() = R.layout.character_item_alternate_layout

    override fun initializeViewBinding(view: View) = CharacterItemAlternateLayoutBinding.bind(view)

}