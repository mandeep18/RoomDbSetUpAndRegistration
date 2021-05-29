package com.kickhead.registrationwithdb.screens.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kickhead.registrationwithdb.R
import com.kickhead.registrationwithdb.database.AppDatabase
import com.kickhead.registrationwithdb.model.TabModel
import com.kickhead.registrationwithdb.screens.fragments.RegistrationFragment
import com.kickhead.registrationwithdb.screens.fragments.UsersListFragment
import com.kickhead.registrationwithdb.utils.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var mAppDatabase: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createListForTabs()


//
//        mAppDatabase?.usersDao()?.loadAllChatSessionUsers()?.observe(this, {
//                Log.d("TAG","sdfgfds-${it.size}")
//        })
    }


    private fun createListForTabs(){
        val list = arrayListOf<TabModel>()
        list.add(TabModel(name = "Registration", position = 0))
        list.add(TabModel(name = "Display", position = 1))

        setUpViewPager(list)
    }

    private fun setUpViewPager(tabList: MutableList<TabModel>){
        val mAdapter = ViewPagerAdapter(manager = supportFragmentManager)
        tabList.forEach {
            when(it.position){
                0 -> mAdapter.addFrag(RegistrationFragment.newInstance(it), it.name)
                1 -> mAdapter.addFrag(UsersListFragment.newInstance(it), it.name)
            }
        }
        viewPagerCategory?.apply {
            adapter = mAdapter
            offscreenPageLimit = mAdapter.count
            isSaveEnabled = false
        }
        tabLayout?.post(Runnable {
            tabLayout?.setupWithViewPager(viewPagerCategory)
        })
    }

}