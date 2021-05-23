package vijay.app.dona.utils

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import vijay.app.dona.R
import vijay.app.dona.utils.listeners.OnSwipeCallback
import vijay.app.dona.utils.viewholder.TaskViewHolder
import vijay.app.dona.utils.listeners.ItemTouchHelperContract

class ItemMoveCallBack(private val mAdapter: ItemTouchHelperContract,
                       private val swipeListener: OnSwipeCallback,
                       private val mContext: Context) :
    ItemTouchHelper.Callback() {
    private val TAG = ItemMoveCallBack::class.java.name

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag : Int = (ItemTouchHelper.UP or ItemTouchHelper.DOWN)
        val swipeFlags : Int = (ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT)
        return makeMovementFlags(dragFlag, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter.onRowMove(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.absoluteAdapterPosition
        //val position = viewHolder.absoluteAdapterPosition
        when(direction) {
            ItemTouchHelper.LEFT -> {
                swipeListener.editTask(position)
            }
            ItemTouchHelper.RIGHT -> {
                swipeListener.deleteTask(position)
            }
        }
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if(viewHolder is TaskViewHolder) {
                mAdapter.onRowSelected(viewHolder)
            }
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if(viewHolder is TaskViewHolder) {
            mAdapter.onRowClear(viewHolder)
        }
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
            .addSwipeLeftActionIcon(R.drawable.ic_pencil)
            .addSwipeLeftBackgroundColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.blue_300
                )
            )
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