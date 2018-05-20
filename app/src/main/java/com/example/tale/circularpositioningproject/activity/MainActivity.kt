package com.example.tale.circularpositioningproject.activity

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.tale.circularpositioningproject.R
import com.example.tale.circularpositioningproject.databinding.ActivityMainBinding
import com.example.tale.circularpositioningproject.fragment.ClockFragment
import com.example.tale.circularpositioningproject.fragment.DoubleRollingFragment
import com.example.tale.circularpositioningproject.fragment.RollingClockFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val clockFragment = ClockFragment.newInstance()

        transitionFragment(clockFragment)
    }

    fun transitionFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item ?: return true

        when (item.itemId) {
            R.id.menu1 -> {
                val fragment = ClockFragment.newInstance()
                transitionFragment(fragment)
            }
            R.id.menu2 -> {
                val fragment = RollingClockFragment.newInstance()
                transitionFragment(fragment)
            }
            R.id.menu3 -> {
                val fragment = DoubleRollingFragment.newInstance()
                transitionFragment(fragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
