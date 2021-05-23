package vijay.app.dona.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.ui.MainViewModel
import vijay.app.dona.ui.dialog.AddTaskBottomSheet
import vijay.app.dona.ui.dialog.DeleteDialog
import vijay.app.dona.utils.Constants
import vijay.app.dona.utils.Constants.Companion.TaskCategory.TODAY
import vijay.app.dona.utils.ItemMoveCallBack
import vijay.app.dona.utils.SmartPreferences
import vijay.app.dona.utils.TimeUtil
import vijay.app.dona.utils.adapter.TaskAdapter
import vijay.app.dona.utils.listeners.*


class HomeFragment : Fragment(), TaskStatusChanged {
    private val TAG = HomeFragment::class.java.name

    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: TaskAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var emptyState: ViewStub
    private lateinit var viewModel: MainViewModel
    lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var taskList: ArrayList<ToDoTask>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        configureVariable()
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")

        initView(view)
        buildRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        if (TimeUtil.isDateChanged(requireContext())) {
            // add recurring task
            // update the previous date to the current date
            // move all the completed task to completed section
            viewModel.addRecurringTask()
            viewModel.removeCompletedTask()
        }
        //viewModel.removeCompletedTask()

        //subscribeToUi
        subscribeToUi(viewModel.getObservableTask(TODAY.category))
    }

    private fun subscribeToUi(allTask: LiveData<List<ToDoTask>>) {
        allTask.observe(viewLifecycleOwner, {
            Log.d(TAG, "subscribeToUi: $it")
            taskList = it as ArrayList<ToDoTask>
            adapter.submitList(it)
            adapter.setTasks(taskList)
            if (taskList.isEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
            }
        })
    }

    private fun initView(view: View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        fab = view.findViewById(R.id.fab)
        emptyState = view.findViewById(R.id.emptyStateLayout)

        fab.setOnClickListener {
            showAddTaskDialog(null)      //add new task at zero
        }
    }


    private fun buildRecyclerView() {
        taskList = ArrayList()
        layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        /*val anim = recyclerView.itemAnimator as SimpleItemAnimator
        anim.supportsChangeAnimations = false*/

        adapter = TaskAdapter(requireContext(), this, dragListener)
        recyclerView.adapter = adapter
        val callback = ItemMoveCallBack(adapter, itemSwipeListener, requireContext())
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        /*
       *   below code is used to add scroll animation when adding a new item
       *   https://stackoverflow.com/a/62448327/9944838
       * */
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                if (positionStart == 0 && positionStart == layoutManager.findFirstCompletelyVisibleItemPosition()) {
                    layoutManager.scrollToPosition(0)
                }
            }
        })
    }

    private val itemSwipeListener = object : OnSwipeCallback {
        override fun editTask(position: Int) {
            adapter.notifyItemChanged(position)
            showAddTaskDialog(taskList[position])
        }

        override fun deleteTask(position: Int) {
            adapter.notifyItemChanged(position)
            showDeletePopUp(position)
        }
    }

    private fun showDeletePopUp(position: Int) {
        val deleteCallback = object : DeleteConfirmation {
            override fun onDelete() {
                viewModel.deleteTask(adapter.currentList[position])
            }
        }

        val deleteDialog = DeleteDialog.getDialog(
            requireContext(),
            deleteCallback
        )
        deleteDialog.show()
    }

    private val dragListener = object : StartDragListener {
        override fun startDrag(viewHolder: RecyclerView.ViewHolder) {
            itemTouchHelper.startDrag(viewHolder)
        }
    }

    private fun configureVariable() {
        val textSize = SmartPreferences.getInstance(requireContext())
            .getValue(Constants.STRIKE_THROUGH_TEXT_SIZE, 6)

        //Set Up the values
        Constants.SETTING_TEXT_SIZE = (14 + textSize).toFloat()
    }

    private fun showEmptyState(show: Boolean) {
        if (show) {
            emptyState.visibility = View.VISIBLE
        } else {
            emptyState.visibility = View.GONE
        }
    }

    private fun showAddTaskDialog(task: ToDoTask?) {
        val listener = object : AddTask {
            override fun saveTask(task: ToDoTask, isNew: Boolean) {
                if (isNew) {
                    viewModel.insertTask(task)
                } else {
                    viewModel.updateTask(task)
                }
            }
        }
        val dialog = AddTaskBottomSheet(listener, task, requireContext(), TODAY.category)
        dialog.show(parentFragmentManager, "add_task")
    }

    override fun onStatusChange(task: ToDoTask, position: Int, isCompleted: Boolean) {
        Log.d(TAG, "onClick: state $isCompleted")
        val currentTime = TimeUtil.getCurrentDate()
        Log.d(TAG, "onStatusChange: $currentTime")
        viewModel.changeTaskStateAndTime(task, isCompleted, currentTime)
    }

    override fun updateList(currentList: List<ToDoTask>) {
        taskList = currentList as ArrayList<ToDoTask>
        viewModel.updateTask(taskList)
    }
}

/*
*
*   private fun tempData(): ArrayList<ToDoTask> {
        val list = ArrayList<ToDoTask>()
        for (i in 1..25) {
            val text =
                if (i % 2 == 0) "Hi vijay" else "sdhfjhjhgsdjhjsdhfjhsdkhfksdhfhssdhjsdhfjkhsdhfs "
            val task = ToDoTask(
                text,
                "", "", false, "", "", 0
            )
            list.add(task)
        }
        return list
    }
* */