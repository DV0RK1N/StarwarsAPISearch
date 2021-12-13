package com.example.getthedataforduethealth.character

import com.google.gson.annotations.SerializedName

data class CharacterIntroduction(
    var characterId: Int,
    var characterName: String,
    var characterNickname: String?,
    var characterHeadShot: String
)
