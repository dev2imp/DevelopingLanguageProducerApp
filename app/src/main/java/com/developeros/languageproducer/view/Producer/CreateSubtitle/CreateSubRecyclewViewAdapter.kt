package com.developeros.languageproducer.view.Producer.CreateSubtitle

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.developeros.languageproducer.viewmodel.CreateSubtitleViewModel

class CreateSubRecyclewViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var Items = ArrayList<TimeTextDataModel>()
    var createSubtitleViewModel: CreateSubtitleViewModel
    var removeItemInterface: RemoveUpdateItemInterface
    var context: Context

    constructor(
        context: Activity,
        createSubtitleViewModel: CreateSubtitleViewModel,
        removeItemInterface: RemoveUpdateItemInterface
    ) : super() {
        this.context = context
        this.createSubtitleViewModel = createSubtitleViewModel
        this.removeItemInterface = removeItemInterface
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items_recview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RecViewHolder -> {
                holder.bind(
                    removeItemInterface,
                    Items.get(position),
                    position,
                    createSubtitleViewModel
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return Items.size
    }

    class RecViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            removeItemInterface: RemoveUpdateItemInterface,
            timeTextDataModel: TimeTextDataModel,
            position: Int,
            createSubtitleViewModel: CreateSubtitleViewModel,

            ) {
            var SubTime: EditText = itemView.findViewById(R.id.SubTime)
            var SubText: EditText = itemView.findViewById(R.id.SubText)
            var SubDelete: ImageView = itemView.findViewById(R.id.SubDelete)
            SubTime.setText(timeTextDataModel.Time)
            SubText.setText(timeTextDataModel.Text)
            //set addTextChangeListener to save text to array in repository
            //set Onfocus change
            SubText.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                //as the focus changes save it to back Subtitle array in repository
                var timeTextDatamodel =
                    TimeTextDataModel(SubTime.text.toString(), SubText.text.toString())
                createSubtitleViewModel.UpdateItemAt(timeTextDatamodel, position)
                if (hasFocus) {
                    createSubtitleViewModel.setUpLoopModel(position)


                } else {
                    removeItemInterface.notifyChange(timeTextDatamodel, position)
                }
            })

            SubTime.setOnFocusChangeListener(View.OnFocusChangeListener { v, hasFocus ->
                //as the focus changes save it to back Subtitle array in repository
                var timeTextDatamodel =
                    TimeTextDataModel(SubTime.text.toString(), SubText.text.toString())

                createSubtitleViewModel.UpdateItemAt(timeTextDatamodel, position)
                if (hasFocus) {
                    createSubtitleViewModel.setUpLoopModel(position)
                } else {
                    removeItemInterface.notifyChange(timeTextDatamodel, position)
                }
            })
            SubDelete.setOnClickListener {
                /*
                remove from actual repository or database view Viewmodel
                 */
                createSubtitleViewModel.RemoveAt(position)
                /*
                remove from array in fragment to reset recview in here
                 */
                removeItemInterface.RemoveItemAt(position)
            }
        }
    }
    fun InitilizeByRoom(items: ArrayList<TimeTextDataModel>) {
        Items= ArrayList()
        Items = items
        notifyDataSetChanged()
    }

    fun AddNewItem(item: TimeTextDataModel) {
        Items.add(item)
        notifyItemChanged(Items.size - 1)
    }
    fun UpdateOnRemove(int: Int) {
        Items.removeAt(int)
        notifyDataSetChanged()
    }
    fun notifyChange(item: TimeTextDataModel, position: Int) {
        try {
            Items[position] = item
        }catch (e:Exception){
        }
    }
}