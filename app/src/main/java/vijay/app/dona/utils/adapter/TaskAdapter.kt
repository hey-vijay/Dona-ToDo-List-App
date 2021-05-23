package vijay.app.dona.utils.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.listeners.ItemTouchHelperContract
import vijay.app.dona.utils.listeners.StartDragListener
import vijay.app.dona.utils.listeners.TaskStatusChanged
import vijay.app.dona.utils.viewholder.TaskViewHolder
import java.util.*
import kotlin.collections.ArrayList


class TaskAdapter(
    private val mContext: Context,
    private val listener: TaskStatusChanged,
    private val dragListener: StartDragListener
) :
    ListAdapter<ToDoTask, TaskViewHolder>(DIFF_UTIL), ItemTouchHelperContract {
    val TAG: String = TaskAdapter::class.java.name
    private var taskList = ArrayList<ToDoTask>()

    fun setTasks(list: ArrayList<ToDoTask>) {
        taskList = list
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<ToDoTask>() {
            override fun areItemsTheSame(oldItem: ToDoTask, newItem: ToDoTask): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ToDoTask, newItem: ToDoTask): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_task, parent, false
        )
        return TaskViewHolder(v)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        Log.d(TAG, "onBindViewHolder: task $task")

        holder.setUpData(task, mContext)

        holder.animatedCheckBox.setOnClickListener {
            val state = !holder.animatedCheckBox.isChecked()
            holder.setChecked(state, true)
            listener.onStatusChange(task, position, state)
        }

        holder.dragImage.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                dragListener.startDrag(holder)
            }
            true
        }
    }

    override fun onRowMove(fromPosition: Int, toPosition: Int) {
        Log.d(TAG, "onRowMove: printing taskList $taskList")
        Log.d(TAG, "onRowMove: $fromPosition to $toPosition")
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Log.d(TAG, "onRowMove: down ")
                Collections.swap(taskList, i, i + 1)
                val pos1 = taskList[i].position
                val pos2 = taskList[i + 1].position
                taskList[i].position = pos2
                taskList[i + 1].position = pos1
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Log.d(TAG, "onRowMove: UP ")
                Collections.swap(taskList, i, i - 1)
                val pos1 = taskList[i].position
                val pos2 = taskList[i - 1].position
                taskList[i].position = pos2
                taskList[i - 1].position = pos1
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onRowSelected(viewHolder: TaskViewHolder) {
        viewHolder.cvParentView.alpha = 0.3F
    }

    override fun onRowClear(viewHolder: TaskViewHolder) {
        listener.updateList(taskList)
        viewHolder.cvParentView.alpha = 1.0F
    }



}