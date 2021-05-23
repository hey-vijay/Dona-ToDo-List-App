package vijay.app.dona.utils.listeners

import vijay.app.dona.database.modal.ToDoTask

interface AddTask {
    fun saveTask(task: ToDoTask, isNew: Boolean)
}