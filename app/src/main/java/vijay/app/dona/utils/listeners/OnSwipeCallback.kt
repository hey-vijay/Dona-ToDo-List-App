package vijay.app.dona.utils.listeners

import vijay.app.dona.database.modal.ToDoTask

interface OnSwipeCallback {
    fun editTask(position: Int)
    fun deleteTask(position: Int)
}