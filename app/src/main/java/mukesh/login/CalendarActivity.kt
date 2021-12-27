package mukesh.login

import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import mukesh.login.calendar.model.CalendarDay
import mukesh.login.calendar.model.DayOwner
import mukesh.login.calendar.ui.DayBinder
import mukesh.login.calendar.ui.ViewContainer
import mukesh.login.calendar.utils.yearMonth
import mukesh.login.databinding.ActivityCalendarBinding
import mukesh.login.databinding.DialogAddMeetingBinding
import mukesh.login.databinding.Example1CalendarDayBinding
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.*


interface HasToolbar {
    val toolbar: Toolbar? // Return null to hide the toolbar
}

class CalendarActivity : AppCompatActivity(), HasToolbar {
    override val toolbar: Toolbar?
        get() = null

    private val selectedDates = mutableSetOf<LocalDate>()

    @RequiresApi(Build.VERSION_CODES.O)
    private val today = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    private val monthTitleFormatter = DateTimeFormatter.ofPattern("MMMM")
    lateinit var binding: ActivityCalendarBinding
    var oldTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_calendar)
        binding.apply {
            lifecycleOwner = this@CalendarActivity
//            viewModel = vm
        }
        binding.layoutChildMeeting.addItemMeeting.setOnClickListener {
            showBottomSheet()
        }
/*Calender View Initial Setup Data*/
        val daysOfWeek = daysOfWeekFromLocale()
        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(10)
        val endMonth = currentMonth.plusMonths(10)
        binding.exOneCalendar.setup(startMonth, endMonth, daysOfWeek.first())
        binding.exOneCalendar.scrollToMonth(currentMonth)
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDates.contains(day.date)) {
                            selectedDates.remove(day.date)
                        } else {
                            selectedDates.add(day.date)
                        }
                        binding.exOneCalendar.notifyDayChanged(day)
                    }
                }
            }
        }

        binding.exOneCalendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()
                if (day.owner == DayOwner.THIS_MONTH) {
                    when {
                        selectedDates.contains(day.date) -> {
                            if (oldTextView != null) {
                                oldTextView?.apply {
                                    setTextColorRes(R.color.black)
                                    background = null
                                }
                            }
                            textView.apply {
                                setTextColorRes(R.color.white)
                                setBackgroundResource(R.drawable.example_1_selected_bg)
                                oldTextView = this
                            }

                        }
                        today == day.date -> {
                            textView.setTextColorRes(R.color.example_1_white)
                            textView.setBackgroundResource(R.drawable.example_1_today_bg)
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                        }
                    }
                } else {
                    textView.setTextColorRes(R.color.example_1_white_light)
                    textView.background = null
                }
            }
        }

        binding.exOneCalendar.monthScrollListener = {
            if (binding.exOneCalendar.maxRowCount == 6) {
                binding.exOneYearText.text = it.yearMonth.year.toString()
//                binding.exOneMonthText.text = monthTitleFormatter.format(it.yearMonth)
                val month = monthTitleFormatter.format(it.yearMonth)
                updateMonth(month)
            } else {
                // In week mode, we show the header a bit differently.
                // We show indices with dates from different months since
                // dates overflow and cells in one index can belong to different
                // months/years.
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    val month = firstDate.yearMonth.year.toString()
//                    binding.exOneMonthText.text = monthTitleFormatter.format(firstDate)
                    updateMonth(month)
                } else {
                    val month =
                        "${monthTitleFormatter.format(firstDate)} - ${
                            monthTitleFormatter.format(
                                lastDate
                            )
                        }"
                    updateMonth(month)
                    if (firstDate.year == lastDate.year) {
                        binding.exOneYearText.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.exOneYearText.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }

    }

    private fun updateMonth(month: String?) {
        binding.layoutMonths.children.forEach {
            if (month?.lowercase().toString()
                    .contains((it as TextView).text.toString().lowercase())
            ) {
                (it as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    R.drawable.line_curved_pink,
                )
                binding.hScrollMonths.scrollTo(it.getX().toInt(), 0)
            } else {
                (it as TextView).setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0,
                )
            }
        }
    }

    fun daysOfWeekFromLocale(): Array<DayOfWeek> {
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        var daysOfWeek = DayOfWeek.values()
        // Order `daysOfWeek` array so that firstDayOfWeek is at index 0.
        // Only necessary if firstDayOfWeek != DayOfWeek.MONDAY which has ordinal 0.
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            val rhs = daysOfWeek.sliceArray(firstDayOfWeek.ordinal..daysOfWeek.indices.last)
            val lhs = daysOfWeek.sliceArray(0 until firstDayOfWeek.ordinal)
            daysOfWeek = rhs + lhs
        }
        return daysOfWeek
    }

    fun View.makeVisible() {
        visibility = View.VISIBLE
    }

    fun View.makeInVisible() {
        visibility = View.INVISIBLE
    }

    fun View.makeGone() {
        visibility = View.GONE
    }

    fun dpToPx(dp: Int, context: Context): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
            context.resources.displayMetrics
        ).toInt()

    internal fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return context.layoutInflater.inflate(layoutRes, this, attachToRoot)
    }

    internal val Context.layoutInflater: LayoutInflater
        get() = LayoutInflater.from(this)

    internal val Context.inputMethodManager
        get() = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    internal inline fun Boolean?.orFalse(): Boolean = this ?: false

    internal fun Context.getDrawableCompat(@DrawableRes drawable: Int) =
        ContextCompat.getDrawable(this, drawable)

    internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

    internal fun TextView.setTextColorRes(@ColorRes color: Int) =
        setTextColor(context.getColorCompat(color))

    lateinit var dialogAddEventBinding: DialogAddMeetingBinding
    lateinit var dialog: BottomSheetDialog
    private fun showBottomSheet() {
        dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.dialog_add_meeting, null)
        dialogAddEventBinding = DialogAddMeetingBinding.inflate(layoutInflater)
        dialog.setContentView(dialogAddEventBinding.getRoot())
        dialogAddEventBinding.btnAddEvent.setOnClickListener {
            dialog.dismiss()
        }
        dialogAddEventBinding.timeImgEvent.setOnClickListener {
            showTimePicker()
        }
        dialogAddEventBinding.timeTxtEvent.setOnClickListener {
            showTimePicker()
        }
        dialog.show()
    }

    fun showTimePicker() {
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker: TimePickerDialog
        mTimePicker = TimePickerDialog(
            this@CalendarActivity,
            { timePicker, selectedHour, selectedMinute ->
                dialogAddEventBinding.timeTxtEvent.setText(
                    (if (selectedHour > 12) (selectedHour - 12).toString() else selectedHour.toString())
                            + ":$selectedMinute " + (if (selectedHour >= 12) "PM" else "AM")
                )
            },
            hour,
            minute,
            false
        ) //Yes 24 hour time

        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }
}