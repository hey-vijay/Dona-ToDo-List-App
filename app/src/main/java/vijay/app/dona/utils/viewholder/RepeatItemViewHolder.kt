package vijay.app.dona.utils.viewholder

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.dhaval2404.colorpicker.util.setVisibility
import vijay.app.dona.R
import vijay.app.dona.database.modal.RecurringTask
import vijay.app.dona.utils.Constants

class RepeatItemViewHolder(itemView: View, val context: Context) : RecyclerView.ViewHolder(itemView) {

    private var colorTile: View = itemView.findViewById(R.id.viewColor)
    private var tvTitle: TextView = itemView.findViewById(R.id.taskTitle)
    private var tvCount: TextView = itemView.findViewById(R.id.tvTaskCount)

    fun setUpData(recurringTask: RecurringTask) {
        val task = recurringTask.task
        setTitle(task.title)
        setIconBackgroundColor(task.color)
        updateCount(recurringTask.count)
    }

    private fun setTitle(title: String) {
        tvTitle.text = title
        tvTitle.textSize = getTextSize()
    }

    private fun setIconBackgroundColor(color: String) {
        colorTile.setBackgroundColor(Color.parseColor(color))
    }

    private fun updateCount(count: Int) {
        if(count == 0) {
            tvCount.visibility = View.VISIBLE
            tvCount.text = "!!"
            return
        }
        tvCount.text = count.toString()
        tvCount.visibility = View.VISIBLE
    }

    private fun getTextSize(): Float {
        return Constants.SETTING_TEXT_SIZE
    }

}