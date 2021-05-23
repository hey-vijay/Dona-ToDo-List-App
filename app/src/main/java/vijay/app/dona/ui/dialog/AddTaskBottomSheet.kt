package vijay.app.dona.ui.dialog

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.MaterialColorPickerBottomSheet
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.listener.ColorListener
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import vijay.app.dona.R
import vijay.app.dona.database.modal.ToDoTask
import vijay.app.dona.utils.TimeUtil
import vijay.app.dona.utils.listeners.AddTask

class AddTaskBottomSheet(
    private val listener: AddTask,
    private val toDoTask: ToDoTask?,
    private val mContext: Context,
    private val taskCategory: String
) : BottomSheetDialogFragment() {

    private val TAG = AddTaskBottomSheet::class.java.name

    companion object {
        var LAST_COLOR = "#018FFE"
    }

    //views
    private lateinit var etTaskTitle: EditText
    private lateinit var btSave:TextView
    private lateinit var btAdd:TextView
    private lateinit var circularView : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogThemeNoFloating)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.dialog_add_task, container, false
        )
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        if(toDoTask != null) {
            etTaskTitle.setText(toDoTask.title)
            btAdd.visibility = View.GONE
            if(toDoTask.color.isNotEmpty())
                DrawableCompat.setTint(circularView.background, Color.parseColor(toDoTask.color))
        }

        //Show keyboard
        etTaskTitle.requestFocus()
        val imm: InputMethodManager =
            mContext.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun initView(view: View) {
        etTaskTitle = view.findViewById(R.id.etTaskTitle)
        btSave = view.findViewById(R.id.btSave)
        btAdd = view.findViewById(R.id.btAdd)
        circularView = view.findViewById(R.id.view)

        DrawableCompat.setTint(circularView.background, Color.parseColor(LAST_COLOR))

        circularView.setOnClickListener {
            val drawable = circularView.background
            //DrawableCompat.setTint(drawable,ContextCompat.getColor(mContext, R.color.red_700))

            val dialog = MaterialColorPickerDialog
                .Builder(mContext)        					// Pass Activity Instance
                .setTitle("Choose Color")           		// Default "Choose Color"
                .setColorShape(ColorShape.CIRCLE)   	    // Default ColorShape.CIRCLE
                .setColors(resources.getStringArray(R.array.themeColorHex))
                .setColorSwatch(ColorSwatch._500)   	    // Default ColorSwatch._500
                .setDefaultColor(R.color.colorAccent) 		// Pass Default Color
                .build()

            MaterialColorPickerBottomSheet
                .getInstance(dialog)
                .setColorListener(object: ColorListener {
                    override fun onColorSelected(color: Int, colorHex: String) {
                        Log.d(TAG, "initView: color $color and colorHex $colorHex")
                        LAST_COLOR = colorHex
                        DrawableCompat.setTint(drawable, Color.parseColor(LAST_COLOR))
                    }
                })
                .show(parentFragmentManager, "color picker dialog")
        }

        btSave.setOnClickListener {
            if(validateTask()){
                addTask()
                dismiss()
            } else {
                dismiss()
            }
        }
        btAdd.setOnClickListener{
            if(validateTask()) {
                addTask()
                etTaskTitle.setText("")
            }else {
                Toast.makeText(context, "Do something 😕", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addTask() {
        Log.d("HomeFragmentBottomSheet", "addTask BottomSheet: adding task into $taskCategory")
        val text = etTaskTitle.text.toString().trim()
        val task = ToDoTask(
            text, taskCategory, LAST_COLOR, false, TimeUtil.getCurrentDate(),"", 0)
        if(toDoTask == null) {
            listener.saveTask(task, true)
        } else {
            task.id = toDoTask.id
            task.position = toDoTask.position
            listener.saveTask(task, false)
        }
    }

    private fun validateTask(): Boolean {
        val text = etTaskTitle.text.toString().trim()
        return text.isNotEmpty()
    }
}