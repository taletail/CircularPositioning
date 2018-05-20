package com.example.tale.circularpositioningproject.util

object CircleUtil {
    private const val CIRCLE_RADIUS = 360

    /**
     * index番目の要素の角度を計算して返す
     *
     * @param partition 分割数
     * @param index
     */
    fun computeAngleByIndex(partitions: Int, index: Int): Float {
        val angleUnit = (CIRCLE_RADIUS / partitions).toFloat()
        return index * angleUnit
    }

    fun computeAngleByMinite(minute: Int): Float {
        val angleUnit = (CIRCLE_RADIUS / 60).toFloat()
        return minute * angleUnit
    }

    fun computeAngleByHour(hour: Int, minute: Int): Float {
        val angleUnit = CIRCLE_RADIUS.toFloat() / (12 * 60)
        return (hour * 60 + minute) * angleUnit
    }
}