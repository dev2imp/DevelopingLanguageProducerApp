package com.developeros.languageproducer.view.Adapters
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel

class RecyclewViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    var Items = ArrayList<ConsumerDataModel>()
    var context: Context
    var listenerRecViewadapter: ListenerRecViewadapter
    constructor(
        context: Activity,
        listenerConsumerRecViewadapter: ListenerRecViewadapter
    ) : super() {
        this.context = context
        this.listenerRecViewadapter=listenerConsumerRecViewadapter
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RecViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.items_consumer_recview, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder){
            is RecViewHolder -> {
                holder.bind(
                    context,
                    Items.get(position),
                    position,
                    listenerRecViewadapter
                )
            }
        }
    }
    override fun getItemCount(): Int {
        return Items.size
    }
    class RecViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            context: Context,
            consumerDataModel: ConsumerDataModel,
            position: Int,
            listenerConsumerRecViewadapter: ListenerRecViewadapter
            ){
            var TitleTV: TextView = itemView.findViewById(R.id.VideoTitle)
            var VideoImage: ImageView = itemView.findViewById(R.id.VideoImage)
            TitleTV.setText(consumerDataModel.Title)
            //https://img.youtube.com/vi/pGEgu4w8GEM/0.jpg
            Glide.with(context).load("https://img.youtube.com/vi/${consumerDataModel.VideoID}/0.jpg")
                .circleCrop()
                .into(VideoImage)
            VideoImage.setOnClickListener {
                listenerConsumerRecViewadapter.ClickedItemAt(consumerDataModel)
            }
        }
    }
    // as app runs initilize Items arry of ConsumerDataModel.
    fun SetupArray(item: ArrayList<ConsumerDataModel>){
        Items = item
        notifyDataSetChanged()
    }
}