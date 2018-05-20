package com.example.tale.circularpositioningproject.fragment


import android.animation.ValueAnimator
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView

import com.example.tale.circularpositioningproject.R
import com.example.tale.circularpositioningproject.databinding.FragmentRollingClockBinding
import com.example.tale.circularpositioningproject.util.CircleUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import java.util.concurrent.TimeUnit

class RollingClockFragment : Fragment() {

    private val PARTITIONS = 12

    private lateinit var binding: FragmentRollingClockBinding
    private lateinit var hourHand: ImageView
    private lateinit var minuteHand: ImageView

    private val numberAnimators: MutableList<ValueAnimator> = ArrayList()

    private val observer: Observable<DateTime> =
            Observable.interval(20, TimeUnit.SECONDS)
                    .map { DateTime(System.currentTimeMillis(), DateTimeZone.forOffsetHours(9)) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rolling_clock, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding = FragmentRollingClockBinding.bind(view!!)
        hourHand = binding.hourHand
        minuteHand = binding.minuteHand

        val root = binding.root
        val centralView = binding.centralView

        val radius = resources.getDimensionPixelSize(R.dimen.circle_radius)

        for (i in 1..PARTITIONS) {
            val textView = TextView(context)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.text_size))

            val layoutParams = ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT)

            layoutParams.circleConstraint = centralView.id
            layoutParams.circleAngle = CircleUtil.computeAngleByIndex(PARTITIONS, i)
            layoutParams.circleRadius = radius
            textView.layoutParams = layoutParams

            textView.text = i.toString()

            root.addView(textView)

            numberAnimators.add(animateNumber(textView, layoutParams.circleAngle))
        }

        // 時針、分針の角度の初期値設定
        val now = DateTime(System.currentTimeMillis(), DateTimeZone.forOffsetHours(9))
        setClockSetting(now.hourOfDay, now.minuteOfHour)
    }

    override fun onStart() {
        super.onStart()

        val disposable = observer.subscribe(
                {
                    Log.i(TAG, "hour: " + it.hourOfDay + " minute: " + it.minuteOfHour)
                    setClockSetting(it.hourOfDay, it.minuteOfHour)
                },
                {
                    it.printStackTrace()
                },
                {
                    Log.i(TAG, "complete")
                }
        )

        compositeDisposable.add(disposable)

        // 文字盤を回転させるアニメーション
        numberAnimators.forEach {
            it.start()
        }
    }

    override fun onStop() {
        super.onStop()

        numberAnimators.forEach {
            it.cancel()
        }
        compositeDisposable.clear()
    }

    // 時針、分針を時間に合わせて回転させる
    private fun setClockSetting(hour: Int, minute: Int) {
        // hourは12時間に丸める
        val hour12 = hour % 12

        hourHand = binding.hourHand
        minuteHand = binding.minuteHand

        // 時針、分針を回転させる
        hourHand.rotation = CircleUtil.computeAngleByHour(hour12, minute)
        minuteHand.rotation = CircleUtil.computeAngleByMinite(minute)
    }

    private fun animateNumber(number: TextView, initAngle: Float): ValueAnimator {
        val animator = ValueAnimator.ofInt(0, 359)

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = number.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.circleAngle = initAngle + value
            number.layoutParams = layoutParams
        }

        animator.duration = TimeUnit.SECONDS.toMillis(5)
        animator.interpolator = LinearInterpolator()
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE

        return animator
    }

    companion object {
        val TAG = RollingClockFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = RollingClockFragment().apply {}
    }
}
