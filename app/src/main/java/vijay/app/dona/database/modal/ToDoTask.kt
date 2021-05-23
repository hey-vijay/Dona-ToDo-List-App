package vijay.app.dona.database.modal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "task")
data class ToDoTask(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "color") var color: String,
    @ColumnInfo(name = "state") var state: Boolean,
    @ColumnInfo(name = "date_created") var dateCreated: String,
    @ColumnInfo(name = "date_completed") var dateCompleted: String,
    @ColumnInfo(name = "position") var position: Int
){
    constructor(title: String, category: String, color: String, state: Boolean, dateCreated: String,dateCompleted: String, position: Int)
            : this(0, title, category, color, state, dateCreated, dateCompleted, position)

    constructor(title: String, category: String, position: Int)
            : this(title, category,"", false, "", "", position)

    constructor(title: String, category: String, dateCreated: String, position: Int)
            : this(title, category,"", false, dateCreated, "", position)

    // override fun toString(): String = "\n{id = ${this.id}, title = ${this.title}, position = ${this.position}}"
}
