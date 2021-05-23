package vijay.app.dona.database.modal

import androidx.room.ColumnInfo
import androidx.room.Embedded


/*
*   Task along with the number of times it was completed
* */
data class RecurringTask(
    @Embedded
    val task: ToDoTask,

    @ColumnInfo(name = "num")
    var count: Int
)
