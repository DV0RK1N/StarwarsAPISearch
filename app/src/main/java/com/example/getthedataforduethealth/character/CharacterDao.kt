package com.example.getthedataforduethealth.character

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterDao {
    @Insert
    suspend fun insertCharacter(characterDetails: CharacterDetails): Long

    @Delete
    suspend fun deleteCharacter(characterDetails: CharacterDetails): Int

    @Query("UPDATE character_details_table SET characterNickname = :characterNicknameInput WHERE characterId = :searchQuery")
    suspend fun updateCharacterNickname(characterNicknameInput: String, searchQuery: Int)

    @Query("SELECT characterId, characterName, characterNickname, characterHeadShot FROM character_details_table")
    fun getAllCharacterNames(): LiveData<List<CharacterIntroduction>>

    @Query("SELECT * FROM character_details_table")
    fun getAllCharacters(): LiveData<List<CharacterDetails>>

    @Query("SELECT * FROM character_details_table WHERE characterId =:searchQuery")
    fun getCharacterDetails(searchQuery: Int): LiveData<CharacterDetails>

    @Query("SELECT characterName FROM character_details_table")
    fun getCharacterNameList(): LiveData<List<String>>

    @Query("SELECT characterId, characterName, characterNickname, characterHeadShot FROM character_details_table WHERE characterName LIKE :searchQuery || '%'")
    fun getCharactersFromSearch(searchQuery: String): LiveData<List<CharacterIntroduction>>
}