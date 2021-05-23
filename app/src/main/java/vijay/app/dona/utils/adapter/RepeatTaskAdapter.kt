package vijay.app.dona.utils.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import vijay.app.dona.R
import vijay.app.dona.database.modal.RecurringTask
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.viewholder.RepeatItemViewHolder

class RepeatTaskAdapter(val context:Context) : ListAdapter<RecurringTask, RepeatItemViewHolder>(DIFF_UTIL) {
    val TAG = RepeatTaskAdapter::class.java.name

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<RecurringTask>() {
            override fun areItemsTheSame(oldItem: RecurringTask, newItem: RecurringTask): Boolean {
                return oldItem.task.id == newItem.task.id
            }

            override fun areContentsTheSame(oldItem: RecurringTask, newItem: RecurringTask): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repeat_task, parent, false)
        return RepeatItemViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: RepeatItemViewHolder, position: Int) {
        val recurringTask = getItem(position)
        holder.setUpData(recurringTask)
    }

}