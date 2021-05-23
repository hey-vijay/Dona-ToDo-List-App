package vijay.app.dona.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import vijay.app.dona.database.DonaDatabase
import vijay.app.dona.database.dao.TaskDao
import vijay.app.dona.database.modal.RecurringTask
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.Constants.Companion.TaskCategory
import vijay.app.dona.utils.TimeUtil

class MainViewModel(context: Application) : AndroidViewModel(context) {

    private val TAG = MainViewModel::class.java.name

    private var dao:TaskDao = DonaDatabase.getInstance(context).taskDao()

    fun insertTask(task: ToDoTask) {
        GlobalScope.launch(Dispatchers.IO) {
            val pos = dao.getMaxPosition()
            task.position = pos + 1
            dao.insertTask(task)
        }
    }

    private fun insertTask(task: List<ToDoTask>){
        GlobalScope.launch(Dispatchers.IO) {
            dao.insertTask(task)
        }
    }

    fun deleteTask(task:ToDoTask){
        GlobalScope.launch(Dispatchers.IO) {
            dao.deleteTask(task)
        }
    }

    fun updateTask(task:ToDoTask){
        GlobalScope.launch(Dispatchers.IO) {
            dao.updateTask(task)
        }
    }

    fun changeTaskStateAndTime(task:ToDoTask, isCompleted: Boolean, dateCompleted:String){
        GlobalScope.launch(Dispatchers.IO) {
            dao.changeTaskState(task.id, isCompleted, dateCompleted)
        }
    }

    fun updateTask(tasks: List<ToDoTask>) {
        GlobalScope.launch {
            dao.updateTasks(tasks)
        }
    }

    fun getObservableTask(category: String) : LiveData<List<ToDoTask>> {
            return dao.getLiveTask(category)
    }

    private fun getAllTask(category: String) : List<ToDoTask> {
           return dao.getTaskList(category)
    }

    // return the list of all recurring task which is not currently present on the Today Task's List.
    private suspend fun getInactiveRecurringTask() : List<ToDoTask> {
        return dao.getInactiveRecurringTask(TaskCategory.REPEAT.category)
    }

    fun getRecurringTaskWithCount(): LiveData<List<RecurringTask>> {
        return dao.getTasksWithCount()
    }

    /*
    *   Get list of all the recurring task
    *       check if list is empty or not. If empty return;
    *   if Not empty set position, DefaultState, currentTime
    *   and add all the task to the Today category.
    *
    * */
    fun addRecurringTask() {
        GlobalScope.launch(Dispatchers.IO) {
            val recurringTask = getInactiveRecurringTask()
            if (recurringTask.isNotEmpty()) {
                val newRecurringTask = ArrayList<ToDoTask>()
                var pos = dao.getMaxPosition() + 1
                val currentDate = TimeUtil.getCurrentDate()
                val DEFAULT_STATE = false
                for(task in recurringTask) {
                    val newTask = ToDoTask(task.title, TaskCategory.TODAY.category, task.color,
                        DEFAULT_STATE, currentDate,"", pos++)
                    newRecurringTask.add(newTask)
                }
                insertTask(newRecurringTask)
            }
        }
    }

    /*
    *   Remove all the completed task from the
    *       Today category.
    *   Update their category to Completed
    * */
    fun removeCompletedTask() {
        GlobalScope.launch(Dispatchers.IO) {
            val completedTask = getAllTask(TaskCategory.TODAY.category).filter { it.state }
            val category = TaskCategory.COMPLETED.category
            if(completedTask.isNotEmpty()) {
                for (task in completedTask) {
                    task.category = category
                }
                updateTask(completedTask)
            }
        }
    }
}