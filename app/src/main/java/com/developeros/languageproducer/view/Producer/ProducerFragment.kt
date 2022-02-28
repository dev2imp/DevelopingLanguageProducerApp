package com.developeros.languageproducer.view.Producer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel
import com.developeros.languageproducer.model.ProductionModel.ProducerRepository
import com.developeros.languageproducer.view.Adapters.ListenerRecViewadapter
import com.developeros.languageproducer.view.Adapters.RecyclewViewAdapter
import com.developeros.languageproducer.view.Producer.UploadNewVideo.UploadNewVideoActivity
import com.developeros.languageproducer.viewmodel.AuthenticaitionViewModel
import com.developeros.languageproducer.viewmodel.ProducerViemModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProducerFragment : Fragment(), View.OnClickListener,ListenerRecViewadapter {
    //ViewModels and Repository
    lateinit var producerViemModel: ProducerViemModel
    lateinit var authenticaitionViewModel: AuthenticaitionViewModel
    lateinit var producerRepository: ProducerRepository
    //Widgets
    lateinit var SaveOrExportImport: ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var recyclewViewAdapter: RecyclewViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        var view: View = inflater.inflate(R.layout.fragment_production, container, false)
        SaveOrExportImport = view.findViewById(R.id.SaveOrExportImport)
        SaveOrExportImport.setOnClickListener(this)
        recyclerView=view.findViewById(R.id.RecView)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclewViewAdapter = RecyclewViewAdapter(requireActivity(), this)
        recyclerView.adapter = recyclewViewAdapter
        //initialise producer viewmodel and repository
        producerRepository= ProducerRepository();
        producerViemModel = ProducerViemModel(producerRepository)
        producerViemModel.AllItems.observe(requireActivity(), Observer {
            recyclewViewAdapter.SetupArray(it)
        })
        authenticaitionViewModel =
            AuthenticaitionViewModel((requireActivity().application as Languageproducer))
        //check if user is logged in.
        var User = (requireActivity().application as Languageproducer).auth.currentUser
        if (User == null) {
            val action = ProducerFragmentDirections.actionProducerFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        //refresh token then with the token get
        var token = Firebase.auth.currentUser!!.uid
        producerViemModel.GetItemsFromServer(token);
        //get users Posts from server

        return view
    }
    override fun onClick(v: View) {
        if(v.id==R.id.SaveOrExportImport) {
            showPopup(v);
        }
    }
    fun showPopup(v: View) {
        val popup = PopupMenu(requireActivity(), v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_production_options, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.UploadNewVideoFragment ->{
                    //open activity to uplaod post
                    var intent = Intent(requireActivity(),UploadNewVideoActivity::class.java)
                    startActivity(intent)
                }
                R.id.CreateSubtitleFragment -> {
                    //open activity to create subtitle
                    val action = ProducerFragmentDirections.actionProducerFragmentToCreateSubtitleFragment()
                    findNavController().navigate(action)

                }
                R.id.SignOut -> {
                    //sing out user as user clicks
                    authenticaitionViewModel.SignOut()
                    val action = ProducerFragmentDirections.actionProducerFragmentToLoginFragment()
                    findNavController().navigate(action)
                }
            }
            true
        })
        popup.show()
    }

    override fun ClickedItemAt(consumerDataModel: ConsumerDataModel) {
        //TODO

    }
}