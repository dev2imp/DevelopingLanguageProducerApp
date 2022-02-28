package com.developeros.languageproducer.view.Producer.CreateSubtitle

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.utils.TimeManipulation
import com.developeros.languageproducer.model.CreateSubtitleModel.TimeTextDataModel
import com.developeros.languageproducer.model.CreateSubtitleModel.CreateSubtitleRepository
import com.developeros.languageproducer.model.RoomDatabse.AppSaveDataModel
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.utils.LooperModel
import com.developeros.languageproducer.view.dialogs.DeleteItemDialog
import com.developeros.languageproducer.view.dialogs.DeleteItemDialogListener
import com.developeros.languageproducer.view.dialogs.EnterFileNameDialog
import com.developeros.languageproducer.view.dialogs.EnterFileNameDialogListener
import com.developeros.languageproducer.viewmodel.CreateSubtitleViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.io.*
import kotlin.collections.ArrayList
class CreateSubtitleResultActivity : AppCompatActivity(), View.OnClickListener, YouTubePlayerListener,
    RemoveUpdateItemInterface, DeleteItemDialogListener, EnterFileNameDialogListener{
    //Dialogs
    lateinit var deleteItemDialog: DeleteItemDialog
    lateinit var enterFileNameDialog: EnterFileNameDialog
    var deletThisIndex = -1
    //
    lateinit var youTubePlayer: YouTubePlayerView
    //Repositoreis
    lateinit var createSubtitleRepository: CreateSubtitleRepository
    lateinit var roomDatabaseRepository: RoomDatabaseRepository
    //ViewModel
    lateinit var createSubtitleViewModel: CreateSubtitleViewModel
    //Widgets
    lateinit var SaveOrExportImport: ImageView
    lateinit var CaptureTime: ImageView
    lateinit var SwitchButton: SwitchCompat
    lateinit var RecyclerView: RecyclerView
    lateinit var DurationTextView: TextView
    //Variables
    var VideoUrl = ""
    var VideoId = ""
    var currentSecond = 0.0F //Default current second
    var Loopstate =
        false   //Loop state is false by default if user on switch it will be on. to set looper between two points
    var looperModel = LooperModel(0.0F, 0.5F)
    //ActivityResultLauncher
    //it s new api after startsActivityForresult is deprecated
    lateinit var OpenFileResultLauncher: ActivityResultLauncher<Intent>
    //get seconds in Float and convert time to formated etc
    var timeManipulation = TimeManipulation()
    lateinit var CreateSubRecyclewViewAdapter: CreateSubRecyclewViewAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_subtitle_result)
       var url : String? = intent.getStringExtra("urlToLoadVideo")
        if (url != null) {
            VideoUrl = url
        }
        SaveOrExportImport = findViewById(R.id.SaveOrExportImport)
        SwitchButton = findViewById(R.id.SwitchButton)
        CaptureTime = findViewById(R.id.CaptureTime)
        RecyclerView = findViewById(R.id.RecView)
        RecyclerView.layoutManager = LinearLayoutManager(this)
        youTubePlayer = findViewById(R.id.YouTubePlayer)
        DurationTextView = findViewById(R.id.DurationTextView)
        youTubePlayer.addYouTubePlayerListener(this)
        SaveOrExportImport.setOnClickListener(this)
        CaptureTime.setOnClickListener(this)
        //select file from phone set register activity
        OpenFileResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data.also {uri ->
                    //pass this uri to Viewmodel to access repository and initialize recyclerview and Subtitle array
                    createSubtitleViewModel.InitAlizeOnImportFile(uri)
                }
            }
        }
        //initialize dialog and setup listeners
        deleteItemDialog = DeleteItemDialog()
        deleteItemDialog.SetListener(this)
        enterFileNameDialog = EnterFileNameDialog()
        enterFileNameDialog.SetListener(this)
        // Create  repository
        createSubtitleRepository = CreateSubtitleRepository()
        roomDatabaseRepository =
            RoomDatabaseRepository((application as Languageproducer).database.appDao())
        // initilise  ViewModel
        createSubtitleViewModel =
            CreateSubtitleViewModel(createSubtitleRepository, roomDatabaseRepository)
        //observe data loaded from db
        //define this observer to remove observer when e want to remove
        val allFilesObserver = Observer<AppSaveDataModel> {
            if (it != null) {
                //Put Those saved before to array at repository
                createSubtitleViewModel.InitilizeArray(it)
            }
        }
        //get all subs from db
        createSubtitleViewModel.AllFiles.observe(this, allFilesObserver)
        //as we got all subs from db we want tp o setup Recview
        val InitilzedArrayObserver = Observer<ArrayList<TimeTextDataModel>> {
            //as we got data from db we want to set off observer so that it dpesnt get data once we insert to db
            createSubtitleViewModel.AllFiles.removeObserver(allFilesObserver)
            //Setup on recview as app runs.
            CreateSubRecyclewViewAdapter.InitilizeByRoom(it)
        }
        // observer is InitilzedArrayObserver after initilize we have to remove this observer also
        createSubtitleViewModel.InitilzedArray.observe(this, InitilzedArrayObserver)
        //Observer for Videoid in change
        createSubtitleViewModel.VideoId.observe(this, Observer {
            VideoId = it
        })
        //observe each time user clicks add button
        createSubtitleViewModel.SignleItem.observe(this, Observer {
            //if there was no sub in db than observer is not still removed we have to remove
            createSubtitleViewModel.AllFiles.removeObserver(allFilesObserver)
            CreateSubRecyclewViewAdapter.AddNewItem(it)
        })
        //set Observer on looperModel
        createSubtitleViewModel.looperModel.observe(this, Observer {
            looperModel = it
        })
        createSubtitleViewModel.ShowTimeNow.observe(this, Observer {
            //set current duration
            DurationTextView.text = it
        })
        createSubtitleViewModel.content.observe(this, Observer {
            //as user clicks export in menu and changes content in repository we will get
            //content and put it to file.
            createFile(it.fileName, it.subtitlecontent)
        })
        //if switch is on then LoopState will be true otherwise will be false
        SwitchButton.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            Loopstate = isChecked
        })
        createSubtitleViewModel.YoutubeUrlToVideoId(VideoUrl)
        CreateSubRecyclewViewAdapter = CreateSubRecyclewViewAdapter(this, createSubtitleViewModel, this)
        RecyclerView.adapter = CreateSubRecyclewViewAdapter
    }
    override fun onClick(v: View) {
        if (v.id == R.id.CaptureTime) {
            var timeformated = timeManipulation.SecondToTimeFormated(currentSecond)
            var timeTextDataModel = TimeTextDataModel(timeformated, "")
            createSubtitleViewModel.AddToSubtileArray(timeTextDataModel)
        } else if (v.id == R.id.SaveOrExportImport) {
            showPopup(v)
        }
    }
    fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_save_options, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.SaveWork ->
                    createSubtitleViewModel.InsertFile("${VideoId}")
                R.id.DeletePreviousWork -> {
                    CreateSubRecyclewViewAdapter.InitilizeByRoom(ArrayList())
                    createSubtitleViewModel.ResetSubArrayList()
                    createSubtitleViewModel.DeleteInDb()
                }
                R.id.ExportFile -> {
                    //Export File heres
                    enterFileNameDialog.show(supportFragmentManager, "enterFileNameDialog")
                }
                R.id.ImportFile -> {
                    //Import file here
                    openFile()
                }
            }
            true
        })
        popup.show()
    }

    override fun onApiChange(youTubePlayer: YouTubePlayer) {
        //TODO("Not yet implemented!")
    }
    override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
        currentSecond = second
        if (Loopstate) {
            if (second > looperModel.end) {
                currentSecond = looperModel.start
                youTubePlayer.seekTo(currentSecond)
            }
        }
        //we want to display this time to user we get second in float and convert it string in formated
        createSubtitleViewModel.SetUpShowTimeNow(currentSecond)
    }

    override fun onError(youTubePlayer: YouTubePlayer, error: PlayerConstants.PlayerError) {
        //TODO("Not yet implemented!")
    }
    override fun onPlaybackQualityChange(
        youTubePlayer: YouTubePlayer,
        playbackQuality: PlayerConstants.PlaybackQuality
    ) {
        // TODO("Not yet implemented!")
    }
    override fun onPlaybackRateChange(
        youTubePlayer: YouTubePlayer,
        playbackRate: PlayerConstants.PlaybackRate
    ) {
        // TODO("Not yet implemented!")
    }

    override fun onReady(youTubePlayer: YouTubePlayer) {
        youTubePlayer.loadVideo(VideoId, 0.0F)
    }

    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
        // TODO("Not yet implemented!")
    }

    override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
        looperModel.end = duration
    }

    override fun onVideoId(youTubePlayer: YouTubePlayer, videoId: String) {
        // TODO("Not yet implemented!")
    }

    override fun onVideoLoadedFraction(youTubePlayer: YouTubePlayer, loadedFraction: Float) {
        // TODO("Not yet implemented!")
    }

    override fun RemoveItemAt(position: Int) {
        //we want  to remove this index. first we set global value then show dialog
        deletThisIndex = position
        deleteItemDialog.show(supportFragmentManager, "deleteItemDialog")
    }

    override fun notifyChange(item: TimeTextDataModel, position: Int) {
        CreateSubRecyclewViewAdapter.notifyChange(item, position)
    }

    override fun RemoveItem() {
        //Method of DeleteItemInterface
        //after Getting Delete Confirmation from user ready to delete and Update ui
        CreateSubRecyclewViewAdapter.UpdateOnRemove(deletThisIndex)
    }

    override fun CancelRemoveItem() {
        //Method of DeleteItemInterface
        //as it is cancelled we
        deletThisIndex = -1
    }

    override fun ConfirmSave(filename: String) {
        //  TODO("Not yet implemented")
        createSubtitleViewModel.onExportFile(filename)
    }

    fun createFile(filename: String, subtileContent: String) {
        try {
            var file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "${filename}.srt"
            )
            file.createNewFile()
            var stream = FileOutputStream(file)
            stream.write(subtileContent.toByteArray())
            stream.close()
        } catch (e: Exception) {
        }
    }
    fun openFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        OpenFileResultLauncher.launch(intent)
    }
}






