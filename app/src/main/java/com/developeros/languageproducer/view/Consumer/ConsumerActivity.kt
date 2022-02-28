package com.developeros.languageproducer.view.Consumer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel
import com.developeros.languageproducer.model.ConsumerModel.ConsumerNextTimeCurrentText
import com.developeros.languageproducer.model.ConsumerModel.ConsumerRepository
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.utils.LooperModel
import com.developeros.languageproducer.viewmodel.ConsumerViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.json.JSONObject
class ConsumerActivity:AppCompatActivity(), View.OnClickListener,
    YouTubePlayerListener {
    //ViewModel and Repository
    lateinit var consumerViewModel: ConsumerViewModel
    lateinit var consumerRepository: ConsumerRepository
    lateinit var roomDatabaseRepository: RoomDatabaseRepository
    lateinit var consumerDataModel: ConsumerDataModel
    //YouTubePlayerView
    lateinit var youTubePlayer: YouTubePlayerView
    //Widgets
    lateinit var  ConsumerWidgets:LinearLayout
    lateinit var CurrentSubItem:TextView
    lateinit var switch: SwitchCompat

    //Variables
    var Loopstate = false   //Loop state is false by default if user on switch it will be on. to set looper between two points
    var looperModel = LooperModel(0.0F, 1.0F)
    var currentSecond = 0.0F //Default current second
    var CurrentItem = ConsumerNextTimeCurrentText(0,1.0F,"",0.0F)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer)
        ConsumerWidgets=findViewById(R.id.ConsumerWidgets)
        CurrentSubItem=findViewById(R.id.CurrentSubItem)
        switch=findViewById(R.id.SwitchButton)
        //if switch is on then LoopState will be true otherwise will be false
        switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            Loopstate = isChecked
            //set last LooperModel
            looperModel= LooperModel(CurrentItem.CurrentTime,CurrentItem.NextTime)
        })
        ConsumerWidgets.visibility=View.INVISIBLE
        // TODO
        //  "Title:'$Title', VideoID:'$VideoID',
        //  FirstLanguage:'$FirstLanguage',
        //  SecondLanguage:'$SecondLanguage',
        //  FilePath:'$FilePath'"
        var string : String? = intent.getStringExtra("consumerDataModel")
        if (string != null) {
            var jsonObject  =JSONObject(string)
            var title=  jsonObject.getString("Title")
            var videoid=  jsonObject.getString("VideoID")
            var firstlang=  jsonObject.getString("FirstLanguage")
            var secondlang=  jsonObject.getString("SecondLanguage")
            var filepath=  jsonObject.getString("FilePath")
            consumerDataModel= ConsumerDataModel(title,videoid,firstlang,secondlang,filepath)
            Log.d("consumerDataModel->",consumerDataModel.toString())
        }
        //Setting Up ViewModel with Repository
        consumerRepository = ConsumerRepository()
        roomDatabaseRepository = RoomDatabaseRepository((application as Languageproducer).database.appDao())
        consumerViewModel = ConsumerViewModel(consumerRepository,roomDatabaseRepository)
        consumerViewModel.CurrentItem.observe(this, Observer {
            CurrentItem=it
            CurrentSubItem.setText(CurrentItem.CurrentText)
        })
        consumerViewModel.loopModel.observe(this, Observer {
            looperModel =it
        })
        //YoutubePlayerView
        youTubePlayer = findViewById(R.id.YouTubePlayer)
        youTubePlayer.addYouTubePlayerListener(this)
    }
    override fun onClick(v: View) {
       // TODO("Not yet implemented")
    }
    override fun onApiChange(youTubePlayer: YouTubePlayer) {
       // TODO("Not yet implemented")
    }
    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentSecond = second

        if (Loopstate) {
            if (second > looperModel.end) {
                currentSecond = looperModel.start
                youTubePlayer.seekTo(currentSecond)
            }
        }else{
            if(currentSecond > CurrentItem.NextTime){
                consumerViewModel.SetCurrentSub(CurrentItem.CurrentIndex+1)
            }
        }
    }
    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        //TODO("Not yet implemented")
    }
    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality
    ) {
        //TODO("Not yet implemented")
    }
    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate
    ) {
        //TODO("Not yet implemented")
    }
    override fun onReady(youTubePlayer: YouTubePlayer) {
       // TODO("Not yet implemented")
        youTubePlayer.loadVideo(consumerDataModel.VideoID, 0.0F)
        ConsumerWidgets.visibility=View.VISIBLE
        //ater Video s Loaded try to read file and set current sub
        consumerViewModel.ReadFileFromServer(consumerDataModel.FilePath)
        //by default set 0 for SubTitleArray
        consumerViewModel.SetCurrentSub(0)

    }
    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        //TODO("Not yet implemented")
    }
    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
       // TODO("Not yet implemented")
    }
    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
       // TODO("Not yet implemented")
    }
    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {
       // TODO("Not yet implemented")
    }
}