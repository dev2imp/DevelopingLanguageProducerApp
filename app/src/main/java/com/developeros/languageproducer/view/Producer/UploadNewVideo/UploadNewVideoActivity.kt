package com.developeros.languageproducer.view.Producer.UploadNewVideo
import android.app.Activity
import android.content.ClipboardManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.model.UploadModel.UploadDataModel
import com.developeros.languageproducer.model.UploadModel.UploadRepository
import com.developeros.languageproducer.utils.GetLangsFromBlogSingleton
import com.developeros.languageproducer.viewmodel.UploadViewModel
import org.json.JSONObject
import java.io.File

class UploadNewVideoActivity : AppCompatActivity(),View.OnClickListener {
    //ViewModels
    lateinit var uploadViewModel: UploadViewModel
    //repositories
    lateinit var uploadRepository: UploadRepository
    lateinit var roomDatabaseRepository: RoomDatabaseRepository
    //Widgets
    lateinit var VideoTitle:EditText
    lateinit var VideoUrl:EditText
    lateinit var AttachFile:ImageView
    lateinit var PasteButon:TextView
    lateinit var Upload:Button
    lateinit var AttachedFileName:TextView
    lateinit var LanguageSpinner:Spinner
    lateinit var AddLanguage:TextView
    lateinit var selectedLanguages:TextView
    lateinit var DisplayErrorTextView:TextView
    // Open file
    lateinit var OpenFileResultLauncher: ActivityResultLauncher<Intent>
    //Variables
    var SelectedLangsArr = ArrayList<String>()
    var Title=""
    var VideoId=""
    var FirstLang=""
    var SecondLang=""
    var SubFilePath:Uri?=null
    var Error=""

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_new_video)
        VideoTitle=findViewById(R.id.VideoTitle)
        VideoUrl = findViewById(R.id.VideoUrl)
        AttachFile = findViewById(R.id.AttachFile)
        Upload = findViewById(R.id.Upload)
        PasteButon = findViewById(R.id.PasteButton)
        AttachedFileName= findViewById(R.id.AttachedFileName)
        LanguageSpinner = findViewById(R.id.LanguageSpinner)
        AddLanguage = findViewById(R.id.Add)
        selectedLanguages = findViewById(R.id.SelectedLanguages)
        DisplayErrorTextView=findViewById(R.id.DisplayErrorTextView)
        AddLanguage.setOnClickListener(this)
        AttachFile.setOnClickListener(this)
        Upload.setOnClickListener(this)
        PasteButon.setOnClickListener(this)
        //Setup OpenFileResultLauncher
        OpenFileResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == Activity.RESULT_OK){
                result.data?.data.also { uri ->
                    //getPathfrom the uri later display the file name right text to attach button
                    uri?.let {
                        GetPath(it)
                        //read content of file
                        uploadViewModel.GetFileString(it)
                    }
                }
            }
        }
        //TODO set Observer on langs

        //setUpViewModel and Repository
        uploadRepository = UploadRepository()
        roomDatabaseRepository =
            RoomDatabaseRepository(
                (application as Languageproducer).database.appDao()
            )
        uploadViewModel = UploadViewModel(uploadRepository,roomDatabaseRepository)
        /*
        as App runs in main actiivty once we call to get
        all langs from web and save in an array of Signleton Object.
        
         */
        SetUpSpinner(GetLangsFromBlogSingleton.LangArr)
        uploadViewModel.error.observe(this, Observer {
            DisplayErrorTextView.setText(it)
        })
    }
    fun SetUpSpinner(it: ArrayList<String>) {
        //setting up arraylist
        var adapter: ArrayAdapter<String> = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line, it
        )
        LanguageSpinner.adapter = adapter
    }
    override fun onClick(v: View){
        if(v.id==R.id.Upload){
            if(ReadyToUploadServer()){
                UploadToServer()
            }
        }else if(v.id == R.id.AttachFile){
        //attach a file
            openFile()
        }else if(v.id==R.id.PasteButton){
            val clipBoardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            var url = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
            VideoUrl.setText(url)
        }else if(v.id == R.id.Add){
           var language = LanguageSpinner.selectedItem.toString()
            if(language !in SelectedLangsArr){
                if(SelectedLangsArr.size<2){
                    SelectedLangsArr.add(language)
                }else{
                    SelectedLangsArr.removeAt(0)
                    SelectedLangsArr.add(language)
                }
                var languages=""
               for(lang in SelectedLangsArr){
                   languages=languages+"\n" +lang
               }
                selectedLanguages.text = languages
            }
        }
    }
    fun GetPath(uri:Uri){
        try {
            val file = File(uri?.path.toString().split(":/")[1])
            SubFilePath=uri
            var filename=  file.path.substring(file.path.lastIndexOf("/")+1)
            AttachedFileName.text = filename
        }catch (e:Exception){
        }
    }
    fun ReadyToUploadServer():Boolean{
        DisplayErrorTextView.setText("")
        /*  minimum 1 language,
            is title Set?,
            is Url From youtube?,
            is file attached(Optional)
        */
        Title= VideoTitle.text.toString()
        if(Title.length<3){
            DisplayErrorTextView.setText(getString(R.string.TitleLength))
        }
        var urlText=VideoUrl.text.toString()
        try {
            VideoId=urlText.substring(VideoUrl.text.toString().lastIndexOf("/")+1)
        }catch (e:Exception){}
        if(VideoId.length==0){
            DisplayErrorTextView.setText(getString(R.string.UrlError))
            return false
        }
        if("youtu.be" !in urlText.split("/")){
            DisplayErrorTextView.setText(getString(R.string.YoutubeUrlOnly))
            return false
        }
        if(SelectedLangsArr.size==0){
            DisplayErrorTextView.setText(getString(R.string.AtLeatOneLanguage))
            return false
        }
        if(SelectedLangsArr.size==2){
            FirstLang = SelectedLangsArr[0]
            SecondLang = SelectedLangsArr[1]
        }else{
              FirstLang = SelectedLangsArr[0]
            }
        return true
    }
    fun UploadToServer(){
        /*
            create ViewModel.
            pass data to upload
            finish the activity
         */
       var uploadDataModel = UploadDataModel(Title,VideoId,FirstLang,SecondLang,SubFilePath)
        uploadViewModel.UploadToServer(uploadDataModel)
        finish()
    }
    fun openFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply{
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        OpenFileResultLauncher.launch(intent)
    }
}