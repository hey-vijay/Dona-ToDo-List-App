package vijay.app.dona.utils.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.viewholder.CompletedTaskViewHolder


class CompletedTaskAdapter : ListAdapter<ToDoTask, CompletedTaskViewHolder>(TaskAdapter.DIFF_UTIL) {

    private val TAG: String = CompletedTaskAdapter::class.java.name

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedTaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_completed_task, parent, false)
        return CompletedTaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompletedTaskViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: pos = $position and itemCount $itemCount")
        val task = getItem(position)
        holder.setData(task)
        holder.showDate(true)

        if (position == 0) {
            holder.showDate(true)
            holder.separatorLine.visibility = View.GONE
        } else {
            val previousTask = getItem(position - 1)
            if (task.dateCompleted != previousTask.dateCompleted) {
                holder.showDate(true)
            } else {
                holder.showDate(false)
            }
        }
    }
}