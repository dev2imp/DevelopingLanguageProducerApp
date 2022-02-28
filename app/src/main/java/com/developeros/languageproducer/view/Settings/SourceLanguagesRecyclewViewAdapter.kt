package com.developeros.languageproducer.view.Settings

import android.app.Activity
import android.content.Context
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.developeros.languageproducer.viewmodel.CreateSubtitleViewModel

class SourceLanguagesRecyclewViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {


    var Items = ArrayList<String>()
    var context: Context
    var listener:SettingListener
    constructor(
        context: Activity,
        listener: SettingListener
    ) : super() {
        this.context = context
        this.listener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items_settings_recview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecViewHolder -> {
                holder.bind(
                    Items.get(position),
                    position,
                    listener
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return Items.size
    }
    class RecViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            language: String,
            position: Int,
            listener: SettingListener
            ) {
            var Language: TextView = itemView.findViewById(R.id.LanguageName)
            var Delete: ImageView = itemView.findViewById(R.id.LanguageNameDelete)
            Language.setText(language)
            Delete.setOnClickListener {
               //I have to delete a language as user click trash button.
                listener.RemoveItemAtFromSarr(position)
            }
        }
    }
    /*
    user wants to remove a language that is added
     */
    fun RemoveItemAt(int: Int){
        notifyDataSetChanged()
    }
    // as app runs initilize Items arry of languages.
    fun SetupArray(item: ArrayList<String>){
        Items =item
        notifyDataSetChanged()
        listener.RemoveObserver()
    }
    //as user added new language we want to notify it
    fun AddNewLanguage(language:String){
        if (language !in Items){
            Items.add(language)
            notifyItemChanged(Items.size-1)
        }
    }
}