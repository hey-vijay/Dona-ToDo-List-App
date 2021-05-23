package vijay.app.dona.ui.dialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import vijay.app.dona.R
import vijay.app.dona.utils.listeners.DeleteConfirmation

class DeleteDialog {
    companion object {
        fun getDialog(
            context: Context,
            listener: DeleteConfirmation
        ): AlertDialog {
            val msz = "Delete note?"
            val alertDialog = AlertDialog.Builder(
                context
            )
                .setTitle(R.string.delete)
                .setMessage(msz)
                .setIcon(R.drawable.ic_delete)
                .setPositiveButton(R.string.delete) { dialog, _ ->
                    listener.onDelete()
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
            alertDialog.setOnShowListener {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.red_300     //just to make it intuitive
                    )
                )
            }
            return alertDialog
        }
    }
}