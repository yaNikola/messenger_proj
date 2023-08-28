package com.example.telegram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.telegram.database.AUTH
import com.example.telegram.database.initFirebase
import com.example.telegram.database.initUser
import com.example.telegram.databinding.ActivityMainBinding
import com.example.telegram.ui.screens.main_list.MainListFragment
import com.example.telegram.ui.screens.register.EnterPhoneNumberFragment
import com.example.telegram.ui.objects.AppDrawer
import com.example.telegram.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        //инициализируем объекты для работы с Firebase
        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch { initContacts() }
            initFields()
            initFunc()
        }
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(ContextCompat.checkSelfPermission(APP_ACTIVITY, READ_CONTACT) == PackageManager.PERMISSION_GRANTED) {
            initContacts()
        }
    }
}

