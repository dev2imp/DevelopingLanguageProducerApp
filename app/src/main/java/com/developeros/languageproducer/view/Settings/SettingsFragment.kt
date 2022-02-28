package com.developeros.languageproducer.view.Settings

import android.os.Bundle
import android.os.RecoverySystem
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.model.RoomDatabse.TargetSourceModel
import com.developeros.languageproducer.utils.GetLangsFromBlogSingleton
import com.developeros.languageproducer.viewmodel.SettingsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SettingsFragment : Fragment(R.layout.fragment_settings), View.OnClickListener,
    SettingListener {
    //Repositories
    lateinit var roomDatabaseRepository: RoomDatabaseRepository
    lateinit var settingsViewModel: SettingsViewModel
    lateinit var ChosenLangObserver: Observer<TargetSourceModel>

    //Widgets
    lateinit var addSourceLanguageTextView: LottieAnimationView
    lateinit var SourceLanguageSpinner: Spinner

    //adapters for RecViews
    lateinit var sourceLanguagesRecyclewViewAdapter: SourceLanguagesRecyclewViewAdapter

    //RecViews
    lateinit var sourceLanguagesRecView: RecyclerView

    //Variables
    var Sarr = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        addSourceLanguageTextView = view.findViewById(R.id.AddSourceLanguageButton)
        addSourceLanguageTextView.setOnClickListener(this)
        SourceLanguageSpinner = view.findViewById(R.id.SourceLanguageSpinner)
        //setup source language recview and adapter
        sourceLanguagesRecView = view.findViewById(R.id.SourceLanguagesRecView)
        //
        sourceLanguagesRecView.layoutManager = LinearLayoutManager(requireActivity())
        sourceLanguagesRecyclewViewAdapter =
            SourceLanguagesRecyclewViewAdapter(requireActivity(), this)
        sourceLanguagesRecView.adapter = sourceLanguagesRecyclewViewAdapter

        roomDatabaseRepository =
            RoomDatabaseRepository(
                (requireActivity().application as Languageproducer).database.appDao()
            )
        settingsViewModel = SettingsViewModel(roomDatabaseRepository)
        /*
        as App runs in main actiivty once we call to get
        all langs from web and save in an array of Signleton Object.
         */
        SetUpSpinner(GetLangsFromBlogSingleton.LangArr)
        ChosenLangObserver = Observer<TargetSourceModel> {
            // we have to convert TargetSourcModel to its actual type now it is string
            /*[France, German]
            targetLanguagesRecyclewViewAdapter.SetupArray(it.targetArray)
            */
            // we have to remove Observer on AllLanguages as  below
            // settingsViewModel.AllLanguages.removeObserver(allLanguagesObserver)
            if (it != null) {
                Sarr = ArrayList()
                var SourceArr = it.sourceArray
                    .replace("[", "")
                    .replace("]", "")
                    .replace(" ", "")
                    .split(",")
                if (SourceArr.size == 1) {
                    Sarr.add(SourceArr[0].trim())
                } else {
                    Sarr = SourceArr as ArrayList<String>
                }
                sourceLanguagesRecyclewViewAdapter.SetupArray(Sarr)
            }
        }
        settingsViewModel.ChosenLangsFromDb.observe(requireActivity(), ChosenLangObserver)
        return view
    }

     fun SetUpSpinner(it: ArrayList<String>,) {
        //setting up arraylist
        var adapter: ArrayAdapter<String> =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line, it
            )
        SourceLanguageSpinner.adapter = adapter
    }
    fun AddToDb(sarr: ArrayList<String>) {
        if (sarr.size > 0) {
            var targetSourceModel = TargetSourceModel(sarr)
            settingsViewModel.InsertLanguages(targetSourceModel)
        } else {
            settingsViewModel.DeleteLanguages()
        }
    }

    override fun onClick(v: View) {
        var language = ""
        if (v.id == R.id.AddSourceLanguageButton) {
            language = SourceLanguageSpinner.selectedItem.toString().trim()
            if (language !in Sarr) {
                Sarr.add(language)
                AddToDb(Sarr)
                //sourceLanguagesRecyclewViewAdapter.AddNewLanguage(language)
            }
        }
    }

    /*
    methods of SettingsListener interface to remove Item
     */
    override fun RemoveItemAtFromSarr(position: Int) {
        Sarr.removeAt(position)
        AddToDb(Sarr)
        sourceLanguagesRecyclewViewAdapter.RemoveItemAt(position)
    }

    override fun RemoveObserver() {
        //removeobserver on languages if we need we can remove observer on it.
        // settingsViewModel.AllLanguages.removeObserver(allLanguagesObserver)
    }
}