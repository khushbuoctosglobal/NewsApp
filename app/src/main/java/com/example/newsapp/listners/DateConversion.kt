package com.example.newsapp.listners

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

//date-adapter for data binding - convert date
object DateConversion {
    @JvmStatic
    @BindingAdapter("formattedDate")
    fun setFormattedDate(view: TextView, inputDate: String?) {
        if (inputDate != null) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val date = inputFormat.parse(inputDate)
                val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.US)
                val formattedDate = outputFormat.format(date)
                view.text = formattedDate
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
    }
}
