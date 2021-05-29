package com.kickhead.registrationwithdb.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kickhead.registrationwithdb.R
import com.kickhead.registrationwithdb.database.AppDatabase
import com.kickhead.registrationwithdb.listeners.OnUserItemClickListener
import com.kickhead.registrationwithdb.model.TabModel
import com.kickhead.registrationwithdb.model.UserModel
import com.kunalapk.smartrecyclerview.listener.SmartRecyclerViewListener
import com.kunalapk.smartrecyclerview.view.SmartRecyclerView

class UsersListFragment: Fragment() {
    private var mAppDatabase: AppDatabase? = null
    private var smartRecyclerView: SmartRecyclerView<UserModel>? = null

    companion object {
        var TAG: String = UsersListFragment::class.java.simpleName

        fun newInstance(tabModel: TabModel): UsersListFragment {
            val fragment = UsersListFragment()
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_users_list, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAppDatabase()
        initRecyclerView(view)
        retrieveTasks()


    }

    private fun initAppDatabase() {
        mAppDatabase = AppDatabase.getInstance(requireActivity())
    }

    private fun initRecyclerView(view: View){
        smartRecyclerView = view.findViewById(R.id.rvUsers)
        smartRecyclerView?.apply {
            initSmartRecyclerView(activity = (activity as AppCompatActivity),smartRecyclerViewListener = smartRecyclerViewListener, isPaginated = false)
            setClickListener(onUserItemClickListener)
            isEnabled = false
        }
    }

    private val smartRecyclerViewListener: SmartRecyclerViewListener<UserModel> = object :
        SmartRecyclerViewListener<UserModel> {

        override fun getItemViewType(model: UserModel): Int {
            return 0
        }

        override fun getViewLayout(model: Int): Int {
            return R.layout.item_registered_user
        }

        override fun onLoadNext() {
        }

        override fun onRefresh() {
        }

        override fun setListSize(size: Int) {

        }

    }

    private val onUserItemClickListener: OnUserItemClickListener = object : OnUserItemClickListener{
        override fun onItemClick(userModel: UserModel, position: Int) {
            Toast.makeText(context, "Hello - ${position+1}. ${userModel.name}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun retrieveTasks(){
        mAppDatabase?.usersDao()?.loadAllChatSessionUsers()?.observe(viewLifecycleOwner, {
                Log.d("TAG","sdfgfds-${it.size}")
            smartRecyclerView?.clearItems()
            smartRecyclerView?.addItems(it)
        })
    }
}