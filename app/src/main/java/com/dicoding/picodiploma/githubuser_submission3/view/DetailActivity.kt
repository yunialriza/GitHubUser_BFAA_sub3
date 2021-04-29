package com.dicoding.picodiploma.githubuser_submission3.view

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.githubuser_submission3.R
import com.dicoding.picodiploma.githubuser_submission3.adapter.PagerAdapter
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_AVATAR
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_COMPANY
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_FOLLOWERS
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_FOLLOWING
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_LOCATION
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_REPOSITORY
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_USER
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.COLUMNS_USERNAME
import com.dicoding.picodiploma.githubuser_submission3.database.DatabaseContract.FavColumns.Companion.CONTENT_URI
import com.dicoding.picodiploma.githubuser_submission3.database.FavoriteHelper
import com.dicoding.picodiploma.githubuser_submission3.database.MappingHelper
import com.dicoding.picodiploma.githubuser_submission3.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.githubuser_submission3.model.FavoriteUser
import com.dicoding.picodiploma.githubuser_submission3.model.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@Suppress("CAST_NEVER_SUCCEEDS")
class DetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteHelper: FavoriteHelper
    private var userFavorite: FavoriteUser? = FavoriteUser()
    private var userFavoriteDetail: FavoriteUser? = null
    private var userDetail: User? = null
    private var favToggle: Boolean? = false
    private var position: Int = 0
    private lateinit var uriWithId: Uri

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_FROM_FAVORITE = "extra_favorite"
        const val REQUEST_FAV_ADD = 100
        const val RESULT_FAV_ADD = 101
        const val REQUEST_FAV_DELETE = 201
        const val RESULT_FAV_DELETE = 301
        const val ALERT_DIALOG_DELETE = 20
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }

    private fun setActionBarTitle(title: String) {
        if (supportActionBar != null) {
            supportActionBar?.title = title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

//        setDatabaseToOpenFromHelper()
        uriProvider()

        bindData()
        viewPagerConfig()
        binding.ivFav.setOnClickListener(this)
    }

    private fun uriProvider() {
        val whereFrom = intent.getBooleanExtra(EXTRA_FROM_FAVORITE, false)
        if (whereFrom) {
            val getDetailDataFrom = intent.getParcelableExtra<FavoriteUser>(EXTRA_DETAIL)
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + getDetailDataFrom?.username)
            val cursor = contentResolver?.query(
                uriWithId,
                null,
                null,
                null,
                null
            )
            Log.d("cursorcheck", cursor.toString())

            if (cursor != null) {
                userFavorite = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        } else {
            val getDetailDataFrom = intent.getParcelableExtra<User>(EXTRA_DETAIL)
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + getDetailDataFrom?.username)
            val cursor = contentResolver?.query(
                uriWithId,
                null,
                null,
                null,
                null
            )
            Log.d("cursor check", cursor.toString())

            if (cursor != null) {
                userFavorite = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }
    }

    private fun bindData() {
        val fromFav = intent.getBooleanExtra(EXTRA_FROM_FAVORITE, false)
        if (fromFav) {
            bindFromFavData()
        } else {
            setData()
        }
    }

    private fun bindFromFavData() {
        val ivAvatar = findViewById<ImageView>(R.id.iv_avatar)
        val tvName = findViewById<TextView>(R.id.tv_user)
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        val tvCompany = findViewById<TextView>(R.id.tv_company)
        val tvLocation = findViewById<TextView>(R.id.tv_location)
        val tvFollower = findViewById<TextView>(R.id.followers)
        val tvFollowing = findViewById<TextView>(R.id.following)
        val tvRepo = findViewById<TextView>(R.id.repo)
        val ibFav = findViewById<ImageView>(R.id.iv_fav)

        userFavoriteDetail = intent.getParcelableExtra<User>(EXTRA_DETAIL) as FavoriteUser
        setActionBarTitle(userFavoriteDetail?.username.toString())

        ivAvatar.let {
            Glide.with(this)
                .load(userFavoriteDetail?.avatar)
                .apply(RequestOptions().override(100, 100))
                .into(it)
        }

        //Name
        if (userFavoriteDetail?.user?.isEmpty() == true || userFavoriteDetail?.user?.equals("null") == true) {
            tvName.visibility = View.GONE
        } else {
            tvName.text = userFavoriteDetail?.user
        }

        //User
        tvUsername.text = userFavoriteDetail?.username
        //Company
        if (userFavoriteDetail?.company?.isEmpty() == true || userFavoriteDetail?.company?.equals("null") == true) {
            tvCompany.visibility = View.GONE
        } else {
            tvCompany.text = userFavoriteDetail?.company
        }
        //Location
        if (userFavoriteDetail?.location?.isEmpty() == true || userFavoriteDetail?.location?.equals(
                "null"
            ) == true
        ) {
            tvLocation.visibility = View.GONE
        } else {
            tvLocation.text = userFavoriteDetail?.location
        }
        tvFollower.text = userFavoriteDetail?.followers
        tvFollowing.text = userFavoriteDetail?.following
        tvRepo.text = userFavoriteDetail?.repository
        ibFav.setImageResource(R.drawable.ic_baseline_favorite_24)
    }

    private fun viewPagerConfig() {
        val sectionPagerAdapter = PagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabLayout
        TabLayoutMediator(tabs, viewPager) { tabs, position ->
            tabs.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
    }

    private fun setData() {
        val ivAvatar = findViewById<ImageView>(R.id.iv_avatar)
        val tvName = findViewById<TextView>(R.id.tv_user)
        val tvUsername = findViewById<TextView>(R.id.tv_username)
        val tvCompany = findViewById<TextView>(R.id.tv_company)
        val tvLocation = findViewById<TextView>(R.id.tv_location)
        val tvFollower = findViewById<TextView>(R.id.followers)
        val tvFollowing = findViewById<TextView>(R.id.following)
        val tvRepo = findViewById<TextView>(R.id.repo)
        val ibFav = findViewById<ImageView>(R.id.iv_fav)


        val user = intent.getParcelableExtra<User>(EXTRA_DETAIL) as User
        setActionBarTitle(user.username.toString())

        ivAvatar.let {
            Glide.with(this)
                .load(user.avatar)
                .apply(RequestOptions().override(100, 100))
                .into(it)
        }
        if (user.user?.isEmpty() == true || user.user?.equals("null") == true) {
            tvName.visibility = View.GONE
        } else {
            tvName.text = user.user
        }

        tvUsername.text = user.username

        if (user.company?.isEmpty() == true || user.company?.equals("null") == true) {
            tvCompany.visibility = View.GONE
        } else {
            tvCompany.text = user.company
        }

        if (user.location?.isEmpty() == true || user.location?.equals("null") == true) {
            tvLocation.visibility = View.GONE
        } else {
            tvLocation.text = user.location
        }
        tvFollower.text = user.followers
        tvFollowing.text = user.following
        tvRepo.text = user.repository
        ibFav.setImageResource(R.drawable.ic_baseline_favorite_border_24)

    }

    private fun setDatabaseToOpenFromHelper() {
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(v: View?) {
        val checked: Int = R.drawable.ic_baseline_favorite_24
        val unChecked: Int = R.drawable.ic_baseline_favorite_border_24
        if (v?.id == R.id.iv_fav) {
            val fromFavActivity = intent.getBooleanExtra(EXTRA_FROM_FAVORITE, false)
            if (fromFavActivity) {
                // unFavorite
                if (favToggle == false) {
                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setTitle("Remove From Favorite")
                        .setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("yes") { _, _ ->
                            run {
                                try {
//                                    val getExtra =
//                                        intent.getParcelableExtra<FavoriteUser>(EXTRA_DETAIL) as FavoriteUser
//                                    val getUsername = getExtra.username
//                                    val checkDelete =
//                                        favoriteHelper.deleteById(getUsername.toString())

                                    position = intent.getIntExtra(EXTRA_POSITION, 0)
                                    val checkDelete = contentResolver.delete(uriWithId, null, null)
                                    Log.d("position", position.toString())
                                    Log.d("uri", uriWithId.toString())
                                    Log.d("remove -> FavActivity", checkDelete.toString())
                                    if (checkDelete == 1) {
                                        favToggle = true
                                        val ibFavToggle = findViewById<ImageView>(
                                            R.id.iv_fav
                                        )
                                        ibFavToggle.setImageResource(unChecked)
                                        Toast.makeText(
                                            this,
                                            "Data User Remove",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Data Have Removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.d("exception -> FavDelete", e.message.toString())
                                }
                            }

                        }.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                    val alert = dialogBuilder.create()
                    alert.show()
                    Log.d("dialog Builder", dialogBuilder.toString())
                    Log.d("dialog", alert.toString())

                } else {
                    userFavoriteDetail =
                        intent.getParcelableExtra<FavoriteUser>(EXTRA_DETAIL) as FavoriteUser

                    userFavorite?.username = userFavoriteDetail?.username
                    userFavorite?.user = userFavoriteDetail?.user
                    userFavorite?.avatar = userFavoriteDetail?.avatar
                    userFavorite?.company = userFavoriteDetail?.company
                    userFavorite?.location = userFavoriteDetail?.location
                    userFavorite?.followers = userFavoriteDetail?.followers
                    userFavorite?.following = userFavoriteDetail?.following
                    userFavorite?.repository = userFavoriteDetail?.repository

                    val intent = Intent()
                    intent.putExtra(EXTRA_DETAIL, userFavorite)
                    intent.putExtra(EXTRA_POSITION, position)

                    val values = ContentValues()
                    values.put(COLUMNS_USERNAME, userFavorite?.username)
                    values.put(COLUMNS_USER, userFavorite?.user)
                    values.put(COLUMNS_AVATAR, userFavorite?.avatar)
                    values.put(COLUMNS_COMPANY, userFavorite?.company)
                    values.put(COLUMNS_LOCATION, userFavorite?.location)
                    values.put(COLUMNS_FOLLOWERS, userFavorite?.followers)
                    values.put(COLUMNS_FOLLOWING, userFavorite?.following)
                    values.put(COLUMNS_REPOSITORY, userFavorite?.repository)

//                    val checkInsert = favoriteHelper.insert(values)
                    val checkInsert = contentResolver?.insert(CONTENT_URI, values)
                    Log.d("insert fav", checkInsert.toString())
                    val splitFromInsert =
                        checkInsert.toString().split("/".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val countInsert = Integer.parseInt(splitFromInsert[4])
                    if (countInsert > 0) {
                        Log.d("Data Have Receive", userFavorite.toString())
                        Log.d("Values", values.toString())
                        favToggle = false
                        val ibFavToggle = findViewById<ImageView>(R.id.iv_fav)
                        ibFavToggle.setImageResource(checked)
                        Toast.makeText(this, "Added to favourite list", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Data Failed to added", Toast.LENGTH_SHORT).show()
                    }
                }

            } else {
//                //delete
                if (favToggle == true) {
                    val dialogBuilder = AlertDialog.Builder(this)
                    dialogBuilder.setTitle("Remove From Favorite")
                        .setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("yes") { _, _ ->
                            run {
                                if (ALERT_DIALOG_DELETE == 20) {
//                                    uriProvider()
//                                    val checkDelete =
//                                        favoriteHelper.deleteById(userFavorite?.username.toString())
                                    position = intent.getIntExtra(EXTRA_POSITION, 0)
                                    val checkDelete = contentResolver?.delete(uriWithId, null, null)
                                    Log.d("position", position.toString())
                                    Log.d("have uriId?", uriWithId.toString())
                                    Log.d("remove -> DetailAct", checkDelete.toString())
                                    if (checkDelete == 1) {
                                        favToggle = false
                                        val ibFavToggle = findViewById<ImageView>(
                                            R.id.iv_fav
                                        )
                                        ibFavToggle.setImageResource(unChecked)
                                        Toast.makeText(
                                            this,
                                            "Data User Have Removed",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Data Failed to Remove",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        }.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
                    val alert = dialogBuilder.create()
                    alert.show()
                    Log.d("dialog Builder", dialogBuilder.toString())
                    Log.d("dialog", alert.toString())

                } else {
                    userDetail = intent.getParcelableExtra<User>(EXTRA_DETAIL) as User

                    userFavorite?.username = userDetail?.username
                    userFavorite?.user = userDetail?.user
                    userFavorite?.avatar = userDetail?.avatar
                    userFavorite?.company = userDetail?.company
                    userFavorite?.location = userDetail?.location
                    userFavorite?.followers = userDetail?.followers
                    userFavorite?.following = userDetail?.following
                    userFavorite?.repository = userDetail?.repository

                    val intent = Intent()
                    intent.putExtra(EXTRA_DETAIL, userFavorite)
                    intent.putExtra(EXTRA_POSITION, position)

                    val values = ContentValues()
                    values.put(COLUMNS_USER, userFavorite?.user)
                    values.put(COLUMNS_USERNAME, userFavorite?.username)
                    values.put(COLUMNS_AVATAR, userFavorite?.avatar)
                    values.put(COLUMNS_COMPANY, userFavorite?.company)
                    values.put(COLUMNS_LOCATION, userFavorite?.location)
                    values.put(COLUMNS_FOLLOWERS, userFavorite?.followers)
                    values.put(COLUMNS_FOLLOWING, userFavorite?.following)
                    values.put(COLUMNS_REPOSITORY, userFavorite?.repository)

//                    val checkInsert = favoriteHelper.insert(values)
                    val checkInsert = contentResolver?.insert(CONTENT_URI, values)
                    Log.d("insert fav", checkInsert.toString())
                    val splitFromInsert =
                        checkInsert.toString().split("/".toRegex()).dropLastWhile { it.isEmpty() }
                            .toTypedArray()
                    val countInsert = Integer.parseInt(splitFromInsert[4])
                    if (countInsert > 0) {

                        Log.d("Data Have Receive", userFavorite.toString())
                        Log.d("Values", values.toString())
                        favToggle = true
                        val ibFavToggle = findViewById<ImageView>(R.id.iv_fav)
                        ibFavToggle.setImageResource(checked)
                        Toast.makeText(this, "Added to favourite list", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Data Have Added", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

}