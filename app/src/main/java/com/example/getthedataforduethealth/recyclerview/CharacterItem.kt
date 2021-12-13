package com.example.getthedataforduethealth.recyclerview

import android.view.View
import com.bumptech.glide.Glide
import com.example.getthedataforduethealth.R
import com.example.getthedataforduethealth.character.CharacterIntroduction
import com.example.getthedataforduethealth.databinding.CharacterItemLayoutBinding
import com.xwray.groupie.viewbinding.BindableItem


class CharacterItem(val characterIntroduction: CharacterIntroduction) :
    BindableItem<CharacterItemLayoutBinding>() {
    override fun bind(viewBinding: CharacterItemLayoutBinding, position: Int) {

        Glide.with(viewBinding.root).load(characterIntroduction.characterHeadShot)
            .into(viewBinding.imageViewCharacterPicture)
        if (characterIntroduction.characterNickname != null)
            viewBinding.textViewCharacterNickName.text =
                characterIntroduction.characterNickname
        else
            viewBinding.textViewCharacterNickName.visibility = View.GONE
        viewBinding.textViewCharacterName.text = characterIntroduction.characterName
    }

    override fun getLayout() = R.layout.character_item_layout

    override fun initializeViewBinding(view: View) = CharacterItemLayoutBinding.bind(view)
}