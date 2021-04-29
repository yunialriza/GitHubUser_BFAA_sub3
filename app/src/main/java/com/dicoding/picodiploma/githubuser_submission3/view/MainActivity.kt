package com.dicoding.picodiploma.githubuser_submission3.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.adapter.UserAdapter
import com.dicoding.picodiploma.githubuser_submission3.databinding.ActivityMainBinding
import com.dicoding.picodiploma.githubuser_submission3.model.User
import com.dicoding.picodiploma.githubuser_submission3.viewModel.HomeViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    val listData : ArrayList<User> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userAdapter = UserAdapter()
        userAdapter.notifyDataSetChanged()

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = userAdapter

        showHomeViewModel()
        searchData()
        configHomeViewModel(userAdapter)

    }

    private fun configHomeViewModel(userAdapter: UserAdapter) {
        homeViewModel.getListUser().observe(this, {
            userItemGit ->
            if (userItemGit != null){
                userAdapter.setData(userItemGit)
                showLoading(false)
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

    private fun showHomeViewModel(){
        homeViewModel = HomeViewModel()
        homeViewModel.setDataUser(applicationContext)
        showLoading(true)
    }
    private fun searchData() {
        binding.apply {
            svUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isEmpty()) {
                        return true
                    } else {
                        listData.clear()
                        showLoading(true)
                        homeViewModel.getDataUserSearch(query, applicationContext)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                   if (newText.isEmpty()){
                       showLoading(true)
                       homeViewModel.setDataUser(applicationContext)
                       configHomeViewModel(UserAdapter())
                   }
                    return false
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.setting_menu -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                true
            }
            R.id.fav_menu -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                true
            }
            else -> true
        }
    }
}