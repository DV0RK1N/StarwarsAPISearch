package com.example.getthedataforduethealth.api

import com.example.getthedataforduethealth.character.CharacterDetails
import com.google.gson.annotations.SerializedName

data class CharactersResult(
    @SerializedName("results")
    var results: List<CharacterDetails>
)
