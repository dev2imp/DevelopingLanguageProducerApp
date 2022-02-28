package com.developeros.languageproducer.view.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.developeros.languageproducer.R

class DeleteItemDialog : DialogFragment() {
    lateinit var Listener: DeleteItemDialogListener
    fun SetListener(listener: DeleteItemDialogListener) {
        Listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            var view = inflater.inflate(R.layout.dialog_delete_item, null)
            builder.setView(view)
                .setPositiveButton(R.string.Delete, DialogInterface.OnClickListener { dialog, id ->
                    //positive button clicked
                    Listener.RemoveItem()
                })
                .setNegativeButton(R.string.Cancel, DialogInterface.OnClickListener { dialog, id ->
                    //negative (Cancel ) clicked
                    Listener.CancelRemoveItem()
                })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }
}