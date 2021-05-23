package vijay.app.dona.ui.fragments

import android.content.ClipData
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vijay.app.dona.R
import vijay.app.dona.database.modal.RecurringTask
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.ui.MainViewModel
import vijay.app.dona.ui.dialog.AddTaskBottomSheet
import vijay.app.dona.ui.dialog.DeleteDialog
import vijay.app.dona.utils.Constants.Companion.TaskCategory
import vijay.app.dona.utils.adapter.RepeatTaskAdapter
import vijay.app.dona.utils.listeners.AddTask
import vijay.app.dona.utils.listeners.DeleteConfirmation


class RecurringTaskFragment : Fragment() {
    val TAG = RecurringTaskFragment::class.java.name


    private lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: RepeatTaskAdapter
    private lateinit var fab: FloatingActionButton
    private lateinit var emptyState: RelativeLayout
    private lateinit var mContext: Context

    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recurring_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState )

        mContext = requireContext()
        initView(view)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        //Subscribe to UI
        subscribeToUI(viewModel.getRecurringTaskWithCount())
    }

    private fun subscribeToUI(recurringTaskWithCount: LiveData<List<RecurringTask>>) {
        recurringTaskWithCount.observe(viewLifecycleOwner, {
            Log.d(TAG, "subscribeToUi: $it")
            adapter.submitList(it)

            if (it.isEmpty()) {
                showEmptyState(true)
            } else {
                showEmptyState(false)
            }
        })

    }

    private fun showEmptyState(show: Boolean) {

    }

    private fun initView(view: View) {
        fab = view.findViewById(R.id.fab)
        recyclerView = view.findViewById(R.id.rv_repeatItem)
        emptyState = view.findViewById(R.id.rlEmptyStateLayout)
        adapter = RepeatTaskAdapter(requireContext())
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        //attach click listener
        fab.setOnClickListener {
            showAddTaskDialog(null)
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
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

    private fun showDeletePopUp(position: Int) {
        val deleteCallback = object : DeleteConfirmation {
            override fun onDelete() {
                viewModel.deleteTask(adapter.currentList[position].task)
            }
        }

        val deleteDialog = DeleteDialog.getDialog(
            requireContext(),
            deleteCallback
        )
        deleteDialog.show()
    }

    fun editTask(position: Int) {
        adapter.notifyItemChanged(position)
        showAddTaskDialog(adapter.currentList[position].task)
    }

    fun deleteTask(position: Int) {
        adapter.notifyItemChanged(position)
        showDeletePopUp(position)
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
        val dialog = AddTaskBottomSheet(listener, task, requireContext(), TaskCategory.REPEAT.category)
        dialog.show(childFragmentManager, "add_recurring_task")
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }


        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.absoluteAdapterPosition
            when(direction) {
                ItemTouchHelper.LEFT -> {
                    editTask(position)
                }
                ItemTouchHelper.RIGHT -> {
                    deleteTask(position)
                }
            }
            return
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addSwipeRightActionIcon(R.drawable.ic_delete)
                .addSwipeRightBackgroundColor(
                    ContextCompat.getColor(
                        mContext,
                        R.color.red_300
                    )
                )
                .create()
                .decorate()
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

    }

    private fun tempData(): ArrayList<ToDoTask> {
        val list = ArrayList<ToDoTask>()
        for (i in 1..25) {
            val task = ToDoTask(
                "This task needs to be completed in $i", "", 0)
            list.add(task)
        }
        return list
    }

}