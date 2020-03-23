package com.zhuzichu.android.shared.tools

import java.text.SimpleDateFormat
import java.util.*

object ToolDate {
    fun getTimeShowString(
        milliseconds: Long,
        abbreviate: Boolean
    ): String? {
        val dataString: String
        val timeStringBy24: String
        val currentTime = Date(milliseconds)
        val today = Date()
        val todayStart = Calendar.getInstance()
        todayStart[Calendar.HOUR_OF_DAY] = 0
        todayStart[Calendar.MINUTE] = 0
        todayStart[Calendar.SECOND] = 0
        todayStart[Calendar.MILLISECOND] = 0
        val todaybegin = todayStart.time
        val yesterdaybegin = Date(todaybegin.time - 3600 * 24 * 1000)
        val preyesterday =
            Date(yesterdaybegin.time - 3600 * 24 * 1000)
        dataString = if (!currentTime.before(todaybegin)) {
            "今天"
        } else if (!currentTime.before(yesterdaybegin)) {
            "昨天"
        } else if (!currentTime.before(preyesterday)) {
            "前天"
        } else if (isSevenDay(currentTime, today)) {
            getWeekOfDate(currentTime)
        } else {
            val dateformatter =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateformatter.format(currentTime)
        }
        val timeformatter24 =
            SimpleDateFormat("HH:mm", Locale.getDefault())
        timeStringBy24 = timeformatter24.format(currentTime)
        return if (abbreviate) {
            if (!currentTime.before(todaybegin)) {
                getTodayTimeBucket(currentTime)
            } else {
                dataString
            }
        } else {
            "$dataString $timeStringBy24"
        }
    }

    private fun isSevenDay(d1: Date, d2: Date): Boolean {
        val calendar = Calendar.getInstance() //得到日历
        calendar.time = d2 //把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -7) //设置为7天前
        val before7days = calendar.time //得到7天前的时间
        return before7days.time < d1.time
    }

    private fun getWeekOfDate(date: Date): String {
        val weekDaysName = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        // String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        val calendar = Calendar.getInstance()
        calendar.time = date
        val intWeek = calendar[Calendar.DAY_OF_WEEK] - 1
        return weekDaysName[intWeek]
    }


    private fun getTodayTimeBucket(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val timeformatter0to11 =
            SimpleDateFormat("KK:mm", Locale.getDefault())
        val timeformatter1to12 =
            SimpleDateFormat("hh:mm", Locale.getDefault())
        return when (calendar[Calendar.HOUR_OF_DAY]) {
            in 0..4 -> {
                "凌晨 " + timeformatter0to11.format(date)
            }
            in 5..11 -> {
                "上午 " + timeformatter0to11.format(date)
            }
            in 12..17 -> {
                "下午 " + timeformatter1to12.format(date)
            }
            in 18..23 -> {
                "晚上 " + timeformatter1to12.format(date)
            }
            else -> ""
        }
    }
}