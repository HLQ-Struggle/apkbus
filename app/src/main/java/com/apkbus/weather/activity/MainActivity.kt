package com.apkbus.weather.activity

import android.content.Intent
import android.os.Bundle
import com.apkbus.weather.R
import com.apkbus.weather.base.BaseActivity
import com.apkbus.weather.fragment.CityWeatherFragment
import com.apkbus.weather.service.UpdateService
import com.apkbus.weather.utils.CityUtils
import com.apkbus.weather.utils.showToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var city: String? = ""
    private var province: String? = ""
    private var town: String? = ""
    private var fragment: CityWeatherFragment? = null

    companion object {
        fun action(activity: BaseActivity?, city: String, province: String, town: String?) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("city", city)
            intent.putExtra("province", province)
            intent.putExtra("town", town)
            activity?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        CityUtils.getAllCities(this)
        container.setBackgroundResource(R.drawable.bg3)
        mActivity.setSwipeBackEnable(false)

        if (intent != null) {
            city = intent.getStringExtra("city")
            province = intent.getStringExtra("province")
            town = intent.getStringExtra("town")
        }

        fragment = CityWeatherFragment()
        val bundle = Bundle()
        bundle.putString("city", city)
        bundle.putString("province", province)
        bundle.putString("town", town)
        fragment!!.arguments = bundle
        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        UpdateService.startUpdateService(mActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    private var lastBackPress: Long = 0
    override fun onBackPressed() {
        val time = System.currentTimeMillis()
        if (time - lastBackPress < 2000) {
            System.exit(0)
            android.os.Process.killProcess(android.os.Process.myPid())
            super.onBackPressed()
        } else {
            lastBackPress = time
            showToast(mActivity, "2秒内双击退出“巴士天气”")
        }
    }

}
