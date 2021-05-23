package vijay.app.dona.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.*
import it.emperor.animatedcheckbox.AnimatedCheckBox
import vijay.app.dona.R
import vijay.app.dona.utils.*
import java.util.concurrent.TimeUnit

class SettingsFragment : Fragment(), SeekBar.OnSeekBarChangeListener {
    val TAG = SettingsFragment::class.java.name


    private lateinit var sbTextSize: SeekBar
    private lateinit var exTextView: TextView
    private lateinit var checkBox: AnimatedCheckBox
    private lateinit var switch: SwitchCompat

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)
        switch.setOnCheckedChangeListener(null)
        setUpDefaultVariables()
        switch.setOnCheckedChangeListener(switchOnCheckedListener)
    }

    private fun initView(view: View) {
        sbTextSize = view.findViewById(R.id.sbTextSize)
        exTextView = view.findViewById(R.id.sampleStrikeThroughText)
        checkBox = view.findViewById(R.id.cbTaskStatus)
        switch = view.findViewById(R.id.swTurnOnNotification)

        sbTextSize.progress = getCurrentProgress(Constants.STRIKE_THROUGH_TEXT_SIZE)

        val workInfo = WorkManager.getInstance(requireContext())
            .getWorkInfosByTag(Constants.NOTIFICATION_CHANNEL_TAG)
        Log.d(TAG, "SettingsFragments: ${workInfo.get().size}")

        //Attach listener to it
        sbTextSize.setOnSeekBarChangeListener(this)

        checkBox.setOnChangeListener {
            val workInfo = WorkManager.getInstance(requireContext())
                .getWorkInfosByTag(Constants.NOTIFICATION_CHANNEL_TAG)
            if (it) {
                //oneTimeWork()
                exTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_500))
            } else {
                exTextView.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey_900))
            }
        }
    }

    private fun oneTimeWork() {
        val oneTimeWorkRequest = OneTimeWorkRequestBuilder<NotifyWorker>()
            .build()
        WorkManager.getInstance(requireContext()).enqueue(oneTimeWorkRequest)
        val workInfo = WorkManager.getInstance(requireContext())
            .getWorkInfosByTag(Constants.NOTIFICATION_CHANNEL_TAG)
    }

    private val switchOnCheckedListener = CompoundButton.OnCheckedChangeListener { _, isChecked ->
        Log.d(TAG, "switch state changed to $isChecked: ")
        if (isChecked) {
            Toast.makeText(requireContext(), "Notification On 👍🏼", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Off 🔕", Toast.LENGTH_SHORT).show()
        }
        setPeriodicNotification(isChecked)
        SmartPreferences.getInstance(requireContext())
            .saveValue(Constants.NOTIFICATION_ENABLE, isChecked)
    }

    private fun setPeriodicNotification(enable: Boolean) {
        if (enable) {
            val initialDelay = TimeUtil.getNotificationDelayTime("06:00")       //minutes
            Log.d(TAG,"setPeriodicNotification: delayDuration $initialDelay ${initialDelay / 60}h ${initialDelay % 60}m")
            val workRequest = PeriodicWorkRequestBuilder<NotifyWorker>(
                1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MINUTES)
                .addTag(Constants.NOTIFICATION_CHANNEL_TAG)
                .build()

            WorkManager.getInstance(requireContext())
                .enqueueUniquePeriodicWork(Constants.NOTIFICATION_CHANNEL_TAG, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
        } else {
            Log.d(TAG, "setPeriodicNotification: cancelling all my workRequest")
            WorkManager.getInstance(requireContext())
                .cancelAllWork()

            val workInfo = WorkManager.getInstance(requireContext())
                .getWorkInfosByTag(Constants.NOTIFICATION_CHANNEL_TAG)
            Log.d(TAG, "SettingsFragments: ${workInfo.get().size}")
            Log.d(TAG, "SettingsFragments: ${workInfo.get().get(0).progress}")
            Log.d(TAG, "SettingsFragments: ${workInfo.get().get(0).state}")
            Log.d(TAG, "SettingsFragments: ${workInfo.get().get(0).tags}")
        }
    }

    private fun setUpDefaultVariables() {
        val textSize = SmartPreferences.getInstance(requireContext())
            .getValue(Constants.STRIKE_THROUGH_TEXT_SIZE, 6)

        //Set Up the values
        Constants.SETTING_TEXT_SIZE = SettingsUtils.getTextSize(textSize)
        switch.isChecked = SmartPreferences.getInstance(requireContext())
            .getValue(Constants.NOTIFICATION_ENABLE, false)
        exTextView.textSize = Constants.SETTING_TEXT_SIZE
    }

    override fun onProgressChanged(sb: SeekBar?, p1: Int, p2: Boolean) {
        Log.d(TAG, "onProgressChanged: ${sb!!.id} pos $p1 $p2")
        when (sb.id) {
            R.id.sbTextSize -> {
                exTextView.textSize = SettingsUtils.getTextSize(p1)
            }
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {
        // Do nothing
    }

    override fun onStopTrackingTouch(sb: SeekBar?) {
        //Set the value into shared Pref
        when (sb!!.id) {
            R.id.sbTextSize -> {
                context?.let {
                    SmartPreferences.getInstance(it).saveValue(
                        Constants.STRIKE_THROUGH_TEXT_SIZE,
                        sb.progress
                    )
                }
            }
        }
        setUpDefaultVariables()
    }

    private fun getCurrentProgress(key: String): Int {
        when (key) {
            Constants.STRIKE_THROUGH_TEXT_SIZE -> {
                val textSize = context?.let {
                    SmartPreferences.getInstance(it).getValue(Constants.STRIKE_THROUGH_TEXT_SIZE, 6)
                }
                if (textSize != null) {
                    return textSize
                }
            }
        }
        return 4
    }
}