package vijay.app.dona.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import vijay.app.dona.database.modal.RecurringTask
import vijay.app.dona.database.modal.ToDoTask


@Dao
interface TaskDao {

    @Insert
    suspend fun insertTask(task: ToDoTask)

    @Insert
    suspend fun insertTask(task: List<ToDoTask>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: ToDoTask)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTasks(taskList: List<ToDoTask>)

    @Query("UPDATE task SET state = :isCompleted, date_completed = :dateCompleted WHERE id = :taskId")
    suspend fun changeTaskState(taskId: Long, isCompleted: Boolean, dateCompleted: String)

    @Delete
    suspend fun deleteTask(task: ToDoTask)

    @Delete
    suspend fun deleteTasks(taskList: List<ToDoTask>)

    @Query("SELECT MAX(position) FROM task")
    fun getMaxPosition(): Int

    @Query("SELECT * FROM task WHERE category IS :category ORDER BY position DESC")
    fun getLiveTask(category: String): LiveData<List<ToDoTask>>


    /*
    *   This query return list of all the recurring task
    *       which is not present in Today's category to prevent adding duplicate task
    * */
    @Transaction
    @Query("""
        SELECT * FROM task 
        WHERE category IS :category 
        AND task.title NOT IN 
           (SELECT t.title FROM task AS t 
               WHERE category IS 'today'
               AND t.state = 0)""")
    suspend fun getInactiveRecurringTask(category: String): List<ToDoTask>

    @Query("SELECT * FROM task WHERE category == :category ORDER BY position DESC")
    fun getTaskList(category: String): List<ToDoTask>


    /*
    *  Get List of all the recurring task
    *   along with the count how many times they have completed.
    * */
    @Transaction
    @Query("""
        SELECT M.* , COUNT(*) - 1 AS num 
        FROM task AS M 
        LEFT JOIN task AS N 
        ON M.title = N.title 
        WHERE M.category = 'repeat' AND 
        N.category != 'today'
        GROUP BY M.title
        ORDER BY position DESC
    """)
    fun getTasksWithCount(): LiveData<List<RecurringTask>>
}