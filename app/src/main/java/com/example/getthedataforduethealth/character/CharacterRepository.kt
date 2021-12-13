package com.example.getthedataforduethealth.character

import androidx.lifecycle.LiveData

class CharacterRepository(private val dao: CharacterDao) {
    val characterNames = dao.getAllCharacterNames()
    val characters = dao.getAllCharacters()

    suspend fun insert(characterDetails: CharacterDetails): Long {
        return dao.insertCharacter(characterDetails)
    }

    suspend fun updateCharacterNickname(characterNickname:String, searchQuery: Int){
        return dao.updateCharacterNickname(characterNickname, searchQuery)
    }

    fun getCharacter(searchQuery: Int): LiveData<CharacterDetails> {
        return dao.getCharacterDetails(searchQuery)
    }

    fun getCharacterNameList(): LiveData<List<String>> {
        return dao.getCharacterNameList()
    }

    fun getCharactersFromSearch(searchQuery: String): LiveData<List<CharacterIntroduction>> {
        return dao.getCharactersFromSearch(searchQuery)
    }
}