package vijay.app.dona.utils.viewholder

import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.TimeUtil

class CompletedTaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val TAG = "CompletedTaskViewHolder"
    private var tvTitle: TextView = itemView.findViewById(R.id.taskTitle)
    private var tvDate: TextView = itemView.findViewById(R.id.tvTaskDate)
    var separatorLine: View = itemView.findViewById(R.id.separatorLine)
    private var clDateLayout = itemView.findViewById<ConstraintLayout>(R.id.clDateLayout)

    fun setData(task: ToDoTask) {
        tvTitle.text = task.title
        val dateCompleted = TimeUtil.getEasyDateFormat(task.dateCompleted)
        if(dateCompleted.isEmpty()) {
            if (task.dateCompleted.isEmpty()){
                showDate(false)
                return
            }
            tvDate.text = task.dateCompleted
        } else {
            tvDate.text = dateCompleted
        }
    }

    fun showDate(visible: Boolean) {
        if(visible){
            tvDate.visibility = View.VISIBLE
            separatorLine.visibility = View.VISIBLE
            clDateLayout.visibility = View.VISIBLE
        } else {
            separatorLine.visibility = View.GONE
            tvDate.visibility = View.GONE
            clDateLayout.visibility = View.GONE
        }
    }

}