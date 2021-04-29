package com.dicoding.picodiploma.githubuser_submission3.view

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.adapter.FavoriteAdapter
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuser_submission3.database.FavoriteHelper
import com.dicoding.picodiploma.githubuser_submission3.database.MappingHelper
import com.dicoding.picodiploma.githubuser_submission3.databinding.ActivityFavoriteBinding
import com.dicoding.picodiploma.githubuser_submission3.model.FavoriteUser
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.EXTRA_DETAIL
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.EXTRA_POSITION
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.REQUEST_FAV_ADD
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.REQUEST_FAV_DELETE
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.RESULT_FAV_ADD
import com.dicoding.picodiploma.githubuser_submission3.view.DetailActivity.Companion.RESULT_FAV_DELETE
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(){

    companion object{
        private const val EXTRA_FAV_STATE = "extra_fav_state"
    }
    private lateinit var adapter: FavoriteAdapter
    private var binding: ActivityFavoriteBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setActionBarTitle()

       configRvFav()
        val handlerThread = HandlerThread("DataObserve")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler){
            override fun onChange(self: Boolean) {
                loadFavAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUser>(EXTRA_FAV_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun setActionBarTitle() {
        if (supportActionBar != null) {
            supportActionBar?.title = "Favourite Users"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun loadFavAsync() {
        GlobalScope.launch(Dispatchers.Main){
            binding?.progressBar?.visibility =View.VISIBLE

            val usersFavHelper = FavoriteHelper.getInstance(applicationContext)
            usersFavHelper.open()
            val deferredUsersFav = async(Dispatchers.IO) {
//                 val cursor = favoriteHelper.queryAll()
//                 CONTENT_URI
                val cursor = contentResolver.query(
                    CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val userFav = deferredUsersFav.await()
            binding?.progressBar?.visibility = View.INVISIBLE
            if (userFav.size > 0) {
                adapter.listFavorite = userFav
            } else {
                adapter.listFavorite= ArrayList()
                binding?.rvFav?.let {
                    Snackbar.make(it,
                        getString(R.string.failed_load),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            Log.d("data", data.toString())
            Log.d("request", requestCode.toString())
            when (requestCode) {

                REQUEST_FAV_ADD -> if (resultCode == RESULT_FAV_ADD) {
                    val note =
                        data.getParcelableExtra<FavoriteUser>(EXTRA_DETAIL) as FavoriteUser
                    adapter.addItem(note)
                    binding?.rvFav?.smoothScrollToPosition(adapter.itemCount - 1)
                    binding?.rvFav?.let {
                        Snackbar.make(it,
                            getString(R.string.one_user_added),
                            Snackbar.LENGTH_SHORT).show()
                    }
                }
                REQUEST_FAV_DELETE -> if (resultCode == RESULT_FAV_DELETE) {
                    val position = data.getIntExtra(EXTRA_POSITION, 0)
                    adapter.removeItem(position)
                    binding?.rvFav?.let {
                        Snackbar.make(it,
                            getString(R.string.user_delete),
                            Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun configRvFav() {
        binding?.rvFav?.layoutManager = LinearLayoutManager(this)
        binding?.rvFav?.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        binding?.rvFav?.adapter =adapter
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    // run this func when open again for refresh data
    override fun onResume() {
        super.onResume()
        loadFavAsync()
    }
}