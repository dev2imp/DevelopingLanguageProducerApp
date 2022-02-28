package com.developeros.languageproducer.view.Producer.CreateSubtitle
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.developeros.languageproducer.R
class CreateSubtitleFragment : Fragment(), View.OnClickListener {
    lateinit var YoutubeUrlEditText: EditText
    lateinit var SearchUrlImageView: ImageView
    lateinit var PasteTextView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        var view: View = inflater.inflate(R.layout.fragment_create_subtitle, container, false)
        YoutubeUrlEditText = view.findViewById(R.id.YoutubeUrlEditText)
        SearchUrlImageView = view.findViewById(R.id.SearchUrlImageView)
        PasteTextView = view.findViewById(R.id.PasteButton)
        PasteTextView.setOnClickListener(this)
        SearchUrlImageView.setOnClickListener(this)
        return view
    }
    override fun onClick(v: View) {
        if (v.id == R.id.SearchUrlImageView) {
            var url = YoutubeUrlEditText.text.toString()
            var intent = Intent(requireContext(),CreateSubtitleResultActivity::class.java)
            intent.putExtra("urlToLoadVideo",url)
            startActivity(intent)
        }else if (v.id == R.id.PasteButton) {
            val clipBoardManager =
                requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            var url = clipBoardManager.primaryClip?.getItemAt(0)?.text?.toString()
            YoutubeUrlEditText.setText(url)
        }
    }
}