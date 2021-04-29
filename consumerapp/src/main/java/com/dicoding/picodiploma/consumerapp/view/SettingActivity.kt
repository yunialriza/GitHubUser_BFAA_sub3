package com.dicoding.picodiploma.consumerapp.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.picodiploma.consumerapp.R
import com.dicoding.picodiploma.consumerapp.alarm.AlarmReceiver
import com.dicoding.picodiploma.consumerapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(){

    companion object{
        private const val PREFS_NAME= "alarm_pref"
        private const val SWITCH_CHACKED = "switch_chacked"
    }
    private lateinit var binding: ActivitySettingBinding
    private lateinit var alarmPreference: SharedPreferences
    private lateinit var alarmReceiver: AlarmReceiver

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivitySettingBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val actionBar = supportActionBar
            actionBar?.title = "Setting"
            actionBar?.setDisplayHomeAsUpEnabled(true)

            alarmReceiver = AlarmReceiver()
            alarmPreference = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            setSwitch()
            binding.swDaily.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked){
                    alarmReceiver.setRepeatingAlarm(this,
                    AlarmReceiver.TYPE_DAILY,getString(R.string.message_reminder))
                } else {
                    alarmReceiver.cancelAlarm(this)
                }
                saveChange(isChecked)
            }

        }

    private fun saveChange(value: Boolean) {
        val editor = alarmPreference.edit()
        editor.putBoolean(SWITCH_CHACKED, value)
        editor.apply()
    }

    private fun setSwitch() {
        binding.swDaily.isChecked = alarmPreference.getBoolean(SWITCH_CHACKED, false)
    }

    override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true
        }

    }


