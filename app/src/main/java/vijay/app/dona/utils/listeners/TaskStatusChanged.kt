package vijay.app.dona.utils.listeners

import vijay.app.dona.database.modal.ToDoTask

interface TaskStatusChanged {
    fun onStatusChange(task: ToDoTask, position: Int, isCompleted: Boolean)
    fun updateList(currentList: List<ToDoTask>)
}