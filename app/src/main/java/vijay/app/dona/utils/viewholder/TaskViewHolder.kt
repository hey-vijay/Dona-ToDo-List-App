package vijay.app.dona.utils.viewholder

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.emperor.animatedcheckbox.AnimatedCheckBox
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.Constants

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    //private val MAX_HEIGHT_ATTR = "maxHeight"
    private val TAG = "TaskViewHolder"

    val animatedCheckBox: AnimatedCheckBox = itemView.findViewById(R.id.cbTaskStatus)
    private val taskContent: TextView = itemView.findViewById(R.id.taskTitle)
    val cvParentView: CardView = itemView.findViewById(R.id.cv_parentView)
    val dragImage: ImageView = itemView.findViewById(R.id.dragImage)

    fun setUpData(task: ToDoTask, mContext: Context) {
        var colorHexCode = task.color
        if(colorHexCode.isEmpty()) {
            colorHexCode = "#64B5F6" //default color
        }
        setTaskContent(task.title, task.state, mContext)
        checkBoxColor(colorHexCode)
        setChecked(task.state, false)
    }

    private fun setTaskContent(content: String, state: Boolean, mContext: Context) {
        taskContent.text = content
        taskContent.textSize = getTextSize()
        if (state) {
            taskContent.setTextColor(ContextCompat.getColor(mContext, R.color.grey_500))
            val spannableString = SpannableString(content)
            val strikeThroughSpan = StrikethroughSpan()
            spannableString.setSpan(strikeThroughSpan, 0, content.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            taskContent.text = spannableString
        } else {
            taskContent.setTextColor(ContextCompat.getColor(mContext, R.color.grey_900))
            taskContent.typeface = Typeface.DEFAULT
        }
    }

    private fun checkBoxColor(colorCode: String) {
        animatedCheckBox.borderNotCheckedColor = Color.parseColor(colorCode)
        animatedCheckBox.hookColor = Color.parseColor(colorCode)
    }

    fun setChecked(value: Boolean, animate:Boolean){
        animatedCheckBox.setChecked(value, animate)
    }

    private fun getTextSize(): Float {
        return Constants.SETTING_TEXT_SIZE
    }

    /*private fun collapseView() {
        val startHeight = exTextView.measuredHeight
        exTextView.maxLines = 1
        exTextView.measure(
            View.MeasureSpec.makeMeasureSpec(
                exTextView.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED))

        val endHeight = exTextView.measuredHeight
        val objectAnimator = ObjectAnimator.ofInt(exTextView, MAX_HEIGHT_ATTR, startHeight, endHeight)
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if(exTextView.maxHeight == endHeight){
                    exTextView.maxLines = 1
                }
            }
        })
        objectAnimator.setDuration(400).start()
    }

    private fun expandView() {
        val startHeight = exTextView.measuredHeight
        exTextView.maxLines = 4
        exTextView.measure(
            View.MeasureSpec.makeMeasureSpec(
                exTextView.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED))

        val endHeight = exTextView.measuredHeight
        val objectAnimator = ObjectAnimator.ofInt(exTextView, MAX_HEIGHT_ATTR, startHeight, endHeight)
        objectAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if(exTextView.maxHeight == endHeight){
                    exTextView.maxLines = 4
                }
            }
        })
        objectAnimator.setDuration(450).start()
    }*/
}