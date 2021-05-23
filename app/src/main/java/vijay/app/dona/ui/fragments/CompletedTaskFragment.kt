package vijay.app.dona.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.ui.MainViewModel
import vijay.app.dona.utils.Constants
import vijay.app.dona.utils.adapter.CompletedTaskAdapter


class CompletedTaskFragment : Fragment() {

    private val TAG = CompletedTaskFragment::class.java.name

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvTaskCompleted: TextView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: CompletedTaskAdapter
    private lateinit var emptyState: RelativeLayout

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_completed_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initViews(view)

        //subscribe to UI
        subscribeToUi(viewModel.getObservableTask(Constants.Companion.TaskCategory.COMPLETED.category))
        //adapter.submitList(generateTempData())
    }

    private fun subscribeToUi(completedTask: LiveData<List<ToDoTask>>) {
        completedTask.observe(viewLifecycleOwner, {
            Log.d(TAG, "subscribeToUi: $it")
            for (task in it) Log.d(TAG, "subscribeToUi: dateCompleted ${task.dateCompleted}")
            adapter.submitList(it)
            val taskCompleted = "${it.size} task Completed"
            tvTaskCompleted.text = taskCompleted
            if (it.isEmpty()) {
                emptyState.visibility = View.VISIBLE
            } else {
                emptyState.visibility = View.GONE
            }
        })
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.rv_completedTask)
        emptyState = view.findViewById(R.id.rlEmptyStateLayout)
        tvTaskCompleted = view.findViewById(R.id.tvTaskCompleted)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager

        adapter = CompletedTaskAdapter()
        recyclerView.adapter = adapter
    }

    private fun tempData(): MutableList<ToDoTask> {
        val list = ArrayList<ToDoTask>()
        for (i in 1..20) {
            val newTask = ToDoTask(
                "This task is completed",
                Constants.Companion.TaskCategory.COMPLETED.category,
                i
            )
            list.add(newTask)
        }
        return list
    }

    private fun generateTempData(): List<ToDoTask> {
        val list = ArrayList<ToDoTask>()
        val task =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task1 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task2 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task3 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task4 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task5 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task6 = ToDoTask(
            "new Task change",
            Constants.Companion.TaskCategory.COMPLETED.category,"#64B5F6" ,false, "",
            "12-04-21",
            0
        )
        val task7 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","12-04-21", 0)
        val task8 = ToDoTask(
            "new Task again",
            Constants.Companion.TaskCategory.COMPLETED.category,"#64B5F6" ,false, "",
            "11-04-21",
            0
        )
        val task9 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","11-04-21", 0)
        val task10 = ToDoTask(
            "new Task here",
            Constants.Companion.TaskCategory.COMPLETED.category,"#64B5F6" ,false, "",
            "14-04-21",
            0
        )
        val task11 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","14-04-21", 0)
        val task12 =
            ToDoTask("new Task", Constants.Companion.TaskCategory.COMPLETED.category, "#64B5F6" ,false, "","14-04-21", 0)
        list.add(task)
        list.add(task1)
        list.add(task2)
        list.add(task3)
        list.add(task4)
        list.add(task5)
        list.add(task6)
        list.add(task7)
        list.add(task8)
        list.add(task9)
        list.add(task10)
        list.add(task11)
        list.add(task12)
        return list
    }
}













