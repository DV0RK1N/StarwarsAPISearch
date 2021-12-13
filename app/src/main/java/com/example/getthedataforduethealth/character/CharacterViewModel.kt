package com.example.getthedataforduethealth.character

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.*
import com.example.getthedataforduethealth.api.CharactersResult
import kotlinx.coroutines.launch
import retrofit2.Response

class CharacterViewModel(private val repository: CharacterRepository) : ViewModel(), Observable {
    val characters: LiveData<List<CharacterDetails>>
        get() = repository.characters
    val characterNames: LiveData<List<CharacterIntroduction>>
        get() = repository.characterNames

    @Bindable
    private val characterNicknameTextView = MutableLiveData<String>()

    val getCharacterNickname: MutableLiveData<String>
        get() = characterNicknameTextView

    fun saveCharacterToDatabase(response: Response<CharactersResult>) {
        if (response.body() != null) {
            for (items in response.body()!!.results) {
                insert(items)
            }
        }
    }

    private fun insert(characterDetails: CharacterDetails) = viewModelScope.launch {
        repository.insert(characterDetails)
    }

    fun updateCharacterNickname(characterNickname: String, searchQuery: Int) =
        viewModelScope.launch {
            repository.updateCharacterNickname(characterNickname, searchQuery)
        }

    fun getCharacterFromDatabase(searchQuery: Int): LiveData<CharacterDetails> {
        return repository.getCharacter(searchQuery)
    }

    fun getCharacterNameList(): LiveData<List<String>> {
        return repository.getCharacterNameList()
    }

    fun getCharactersFromSearch(searchQuery: String): LiveData<List<CharacterIntroduction>> {
        return repository.getCharactersFromSearch(searchQuery)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}