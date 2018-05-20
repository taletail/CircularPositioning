package com.example.tale.circularpositioningproject.fragment


import android.animation.ValueAnimator
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button

import com.example.tale.circularpositioningproject.R
import com.example.tale.circularpositioningproject.databinding.FragmentDoubleRollingBinding
import java.util.concurrent.TimeUnit

class DoubleRollingFragment : Fragment() {

    private lateinit var binding: FragmentDoubleRollingBinding

    private lateinit var rollButton: Button

    private lateinit var firstAnimator: ValueAnimator
    private lateinit var secondAnimator: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_double_rolling, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding = FragmentDoubleRollingBinding.bind(view!!)
        binding.fragment = this

        rollButton = binding.rollButton
        val firstView = binding.first
        val secondView = binding.second

        val firstLayoutParams = firstView.layoutParams as ConstraintLayout.LayoutParams
        val secondLayoutParams = secondView.layoutParams as ConstraintLayout.LayoutParams

        firstAnimator = animateView(firstView, firstLayoutParams.circleAngle, TimeUnit.SECONDS.toMillis(5))
        secondAnimator = animateView(secondView, secondLayoutParams.circleAngle, TimeUnit.SECONDS.toMillis(2))
    }

    override fun onStart() {
        super.onStart()

        firstAnimator.start()
    }

    override fun onStop() {
        super.onStop()

        firstAnimator.cancel()
        secondAnimator.cancel()
    }

    private fun animateView(view: View, initAngle: Float, duration: Long): ValueAnimator {
        val animator = ValueAnimator.ofInt(0, 359)

        animator.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.circleAngle = initAngle + value
            view.layoutParams = layoutParams
        }

        animator.duration = duration
        animator.interpolator = LinearInterpolator()
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE

        return animator
    }

    fun onClick(view: View) {
        secondAnimator.start()
    }

    companion object {
        val TAG = DoubleRollingFragment::class.java.simpleName

        @JvmStatic
        fun newInstance() = DoubleRollingFragment().apply {}
    }
}
