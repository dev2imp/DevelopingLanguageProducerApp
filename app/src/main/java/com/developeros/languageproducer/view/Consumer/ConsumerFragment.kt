package com.developeros.languageproducer.view.Consumer

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.developeros.languageproducer.Applicaiton.Languageproducer
import com.developeros.languageproducer.R
import com.developeros.languageproducer.model.ConsumerModel.ConsumerDataModel
import com.developeros.languageproducer.model.ConsumerModel.ConsumerRepository
import com.developeros.languageproducer.model.RoomDatabse.RoomDatabaseRepository
import com.developeros.languageproducer.view.Adapters.RecyclewViewAdapter
import com.developeros.languageproducer.view.Adapters.ListenerRecViewadapter
import com.developeros.languageproducer.viewmodel.ConsumerViewModel

//https://img.youtube.com/vi/pGEgu4w8GEM/0.jpg
class ConsumerFragment : Fragment(), ListenerRecViewadapter {
    //Recyclerview and adapter for it
    lateinit var consumerRecView: RecyclerView
    lateinit var recyclewViewAdapter: RecyclewViewAdapter

    //ViewModel and Repository
    lateinit var consumerViewModel: ConsumerViewModel
    lateinit var consumerRepository: ConsumerRepository
    lateinit var roomDatabaseRepository: RoomDatabaseRepository
    //Widgets
    lateinit var ConsumerDataNotLoad: RelativeLayout
    lateinit var Loading: LottieAnimationView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view: View
        view = inflater.inflate(R.layout.fragment_consumer, container, false)
        //setting UP RecView with Adapter
        consumerRecView = view.findViewById(R.id.ConsumerRecView)
        ConsumerDataNotLoad = view.findViewById(R.id.ConsumerDataNotLoad)
        Loading = view.findViewById(R.id.Loading)
        consumerRecView.layoutManager = LinearLayoutManager(requireActivity())
        recyclewViewAdapter = RecyclewViewAdapter(requireActivity(), this)
        consumerRecView.adapter = recyclewViewAdapter
        //Setting Up ViewModel with Repository
        consumerRepository = ConsumerRepository()
        roomDatabaseRepository =
            RoomDatabaseRepository((requireActivity().application as Languageproducer).database.appDao())
        consumerViewModel = ConsumerViewModel(consumerRepository, roomDatabaseRepository)
        consumerViewModel.GetChosenLangFromRoom.observe(requireActivity(), Observer {
            it?.let {
                ConsumerDataNotLoad.visibility = View.INVISIBLE
                consumerViewModel.GetItemFromServer(
                    it.sourceArray
                        .replace("[", "").replace("]", "")
                )
            }
            if (it == null) {
                //that means user have not selected any language I Have to navigate user to
                //select language fragmant.
                try {
                    findNavController().navigate(ConsumerFragmentDirections.actionConsumerFragmentToSettingsFragment())
                } catch (e: Exception) {
                }
            }
        })
        consumerViewModel.ConsumerItems.observe(requireActivity(), Observer {
            recyclewViewAdapter.SetupArray(it as ArrayList<ConsumerDataModel>)
        })
        return view
    }
    override fun ClickedItemAt(consumerDataModel: ConsumerDataModel) {
        var intent = Intent(requireActivity(), ConsumerActivity::class.java)
        intent.putExtra("consumerDataModel", consumerDataModel.toString())
        startActivity(intent)
    }
}