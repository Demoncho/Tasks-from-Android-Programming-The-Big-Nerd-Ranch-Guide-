package com.example.criminalintent

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

private const val ARG_TIME = "date"

class TimePickerFragment : DialogFragment() {
    interface Callbacks  {
        fun onTimeSelected(time: Date)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val date = arguments?.getSerializable(ARG_TIME) as Date
        calendar.time = date

        val timeListener = TimePickerDialog.OnTimeSetListener {
                _: TimePicker, hour: Int, min: Int ->
            val resultDate : Date = GregorianCalendar(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hour,
                min
            ).time
            targetFragment?.let { fragment ->
                (fragment as Callbacks).onTimeSelected(resultDate)
            }
        }

        val initialHour = calendar.get(Calendar.HOUR)
        val initialMinute = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            true
        )
    }

    companion object {
        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }
            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}