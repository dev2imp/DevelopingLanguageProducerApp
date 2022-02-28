package com.developeros.languageproducer.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.developeros.languageproducer.R

class EnterFileNameDialog : DialogFragment() {
    lateinit var Listener: EnterFileNameDialogListener
    fun SetListener(listener: EnterFileNameDialogListener) {
        Listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var view = inflater.inflate(R.layout.dialog_enter_file_name, null)
            var EnterFileName = view.findViewById<EditText>(R.id.EnterFileName)
            builder.setView(view)
                .setPositiveButton(R.string.Save, DialogInterface.OnClickListener { dialog, id ->
                    //positive button clicked
                    Listener.ConfirmSave(EnterFileName.text.toString())
                })
                .setNegativeButton(R.string.Cancel, DialogInterface.OnClickListener { dialog, id ->
                    //negative (Cancel ) clicked

                })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}