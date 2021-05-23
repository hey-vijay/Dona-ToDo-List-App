package vijay.app.dona.utils.listeners

import vijay.app.dona.utils.viewholder.TaskViewHolder

interface ItemTouchHelperContract {
    fun onRowMove(fromPosition: Int, toPosition:Int)
    fun onRowSelected(viewHolder: TaskViewHolder)
    fun onRowClear(viewHolder: TaskViewHolder)
}