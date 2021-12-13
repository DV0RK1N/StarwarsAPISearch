package com.example.getthedataforduethealth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Fade
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.getthedataforduethealth.api.RetrofitInstance
import com.example.getthedataforduethealth.objects.AppConstants
import com.example.getthedataforduethealth.objects.Coroutines
import com.example.getthedataforduethealth.character.*
import com.example.getthedataforduethealth.databinding.ActivityMainBinding
import com.example.getthedataforduethealth.recyclerview.AlternateCharacterItem
import com.example.getthedataforduethealth.recyclerview.CharacterItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.OnItemClickListener
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"
const val INFORMATION_LOADED = "information_loaded"

class MainActivity : AppCompatActivity() {
    private lateinit var groupAdapter: GroupAdapter<GroupieViewHolder>
    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var characterDao: CharacterDao
    private lateinit var adapter: ArrayAdapter<String>
    private var layoutBigPicture = false
    private var pathChosen = false
    private var informationLoaded = false

    /*TODO:
    *   Create Database
    *       BoilerPlate Code - Object, Dao, Database, Repository, ViewModel, ViewModel Factory,
    *           Main activity with RecyclerView
    *               Object - Character Name (Optional) if user changes Character name, display original name in smaller text next to new name
    *   Get Data From Api
    *   Display/ UI
    *       Main UI - RecyclerView
    *       Introductory Screen that introduces the user to the App (Button with - Let's start this thing)
    *       RecyclerView Item
    *       Single Character View
    *       Transition between recyclerview to single character view
    *   Search for Data
    *   Alter Data
    *  */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        sharedPreferences = getSharedPreferences(AppConstants.SHARED_PREFS, MODE_PRIVATE)
        layoutBigPicture = sharedPreferences.getBoolean(AppConstants.LAYOUT_CHOICE, false)
        val fade = Fade()
        autoCompleteTextView = findViewById(R.id.toolbar_character_name)
        characterDao = CharacterDatabase.getInstance(application).characterDao
        val repository = CharacterRepository(characterDao)
        val factory = CharacterViewModelFactory(repository)
        characterViewModel = ViewModelProvider(this, factory)[CharacterViewModel::class.java]
        binding.characterViewModel = characterViewModel
        binding.lifecycleOwner = this
        groupAdapter = GroupAdapter<GroupieViewHolder>().apply {
            spanCount = 3
        }
        title = "Main Activity"

        window.enterTransition = fade
        window.exitTransition = fade

        bindUi()
        informationLoaded = sharedPreferences.getBoolean(INFORMATION_LOADED, false)
        if (!informationLoaded)
            getDataFromAPI()

        characterViewModel.getCharacterNameList().observe(this) {
            adapter = ArrayAdapter<String>(
                this,
                R.layout.custom_character_list_item,
                R.id.search_character_name,
                it.toSet().toList()
            )
            autoCompleteTextView.setAdapter(adapter)
        }
        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                characterViewModel.getCharactersFromSearch(s.toString())
                    .observe(this@MainActivity) {
                        groupAdapter.update(it.toSet().toList().toCharacterItem())
                    }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        characterViewModel.characterNames.observe(this) {
            if (layoutBigPicture)
                groupAdapter.update(it.toCharacterItem())
            else
                groupAdapter.update(it.toAlternateCharacterItem())
        }
    }

    private fun bindUi() = Coroutines.main {
        characterViewModel.characterNames.observe(this) {
            if (layoutBigPicture)
                initRecyclerView(it.toCharacterItem())
            else
                initAlternateRecyclerView(it.toAlternateCharacterItem())
        }
    }

    private fun initRecyclerView(characterItem: List<CharacterItem>) = binding.recyclerView.apply {
        layoutManager = GridLayoutManager(this@MainActivity, groupAdapter.spanCount).apply {
            spanSizeLookup = groupAdapter.spanSizeLookup
        }

        adapter = groupAdapter.apply {
            addAll(characterItem)
            setOnItemClickListener(onItemClick)
        }
    }

    private fun initAlternateRecyclerView(AlternateCharacterItem: List<AlternateCharacterItem>) =
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = groupAdapter.apply {
                addAll(AlternateCharacterItem)
                setOnItemClickListener(onItemClick)
            }
        }

    private fun List<CharacterIntroduction>.toCharacterItem(): List<CharacterItem> {
        return this.map {
            CharacterItem(it)
        }
    }

    private fun List<CharacterIntroduction>.toAlternateCharacterItem(): List<AlternateCharacterItem> {
        return this.map {
            AlternateCharacterItem(it)
        }
    }

    private val onItemClick = OnItemClickListener { item, _ ->
        if (item is CharacterItem) {
            val intent = Intent(this, CharacterDetailsActivity::class.java)
            val imageView = findViewById<ImageView>(R.id.image_view_character_picture)
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity,
                    Pair.create(imageView, imageView.transitionName)
                )
            intent.putExtra(AppConstants.CHARACTER_ID, item.characterIntroduction.characterId)
            startActivity(intent, options.toBundle())
        } else if (item is AlternateCharacterItem) {
            val intent = Intent(this, CharacterDetailsActivity::class.java)
            val imageView = findViewById<ImageView>(R.id.image_view_character_picture_alternate)
            val options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@MainActivity,
                    Pair.create(imageView, imageView.transitionName)
                )
            intent.putExtra(AppConstants.CHARACTER_ID, item.characterIntroduction.characterId)
            startActivity(intent, options.toBundle())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_reset_recycler_view) {
            val editor = sharedPreferences.edit()
            layoutBigPicture = false
            pathChosen = false
            editor.putBoolean(AppConstants.LAYOUT_CHOICE, layoutBigPicture)
            editor.putBoolean(AppConstants.LAYOUT_CHOSEN, pathChosen)
            editor.apply()
            returnToIntroActivity()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun returnToIntroActivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun getDataFromAPI() {
        val editor = sharedPreferences.edit()
        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getCharacters()
            } catch (e: IOException) {
                Log.e(TAG, "IOException: May not have Internet")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            } catch (e: HttpException) {
                Log.e(TAG, "HttpException: unexpected response")
                binding.progressBar.isVisible = false
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                //TODO: make insert function in ViewModel and call it here to get all data
                characterViewModel.saveCharacterToDatabase(response)
            } else
                Log.e(TAG, "Response not successful")
            binding.progressBar.isVisible = false
            informationLoaded = true
            editor.putBoolean(INFORMATION_LOADED, informationLoaded)
            editor.apply()
        }
    }
}