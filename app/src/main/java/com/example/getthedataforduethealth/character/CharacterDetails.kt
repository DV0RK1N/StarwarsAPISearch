package com.example.getthedataforduethealth.character

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "character_details_table")
data class CharacterDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "characterId")
    @SerializedName("results")
    var characterId: Int = 0,
    @ColumnInfo(name = "characterName")
    @SerializedName("name")
    var characterName: String,
    @ColumnInfo(name = "characterNickname")
    var characterNickname: String? = "Not Set",
    @ColumnInfo(name = "character_height")
    @SerializedName("height")
    var characterHeight: String,
    @ColumnInfo(name = "character_weight")
    @SerializedName("mass")
    var characterWeight: String,
    @ColumnInfo(name = "character_hair_color")
    @SerializedName("hair_color")
    var characterHairColor: String,
    @ColumnInfo(name = "character_skin_color")
    @SerializedName("skin_color")
    var characterSkinColor: String,
    @ColumnInfo(name = "character_eye_color")
    @SerializedName("eye_color")
    var characterEyeColor: String,
    @ColumnInfo(name = "character_birth_year")
    @SerializedName("birth_year")
    var characterBirthYear: String,
    @ColumnInfo(name = "character_gender")
    @SerializedName("gender")
    var characterGender: String,
    @ColumnInfo(name = "characterHeadShot")
    @SerializedName("image")
    var characterHeadShot: String
)
