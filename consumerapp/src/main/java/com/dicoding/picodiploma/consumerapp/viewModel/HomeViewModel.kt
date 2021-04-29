package com.dicoding.picodiploma.consumerapp.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.consumerapp.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class HomeViewModel : ViewModel() {

    private val listDataUser = MutableLiveData<ArrayList<User>>()
    private lateinit var errorMessage : String
    val listItems = ArrayList<User>()

    fun setDataUser(context: Context) {

        val url = "https://api.github.com/users"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token cb0060b3260e1505240db3dddcf3c526204d18e2")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONArray(result)
                    listItems.clear()
                    for (i in 0 until responseObject.length()) {
                        val userGit = responseObject.getJSONObject(i)
                        val username = userGit.getString("login")
                        setDataUserDetail(username, context)
                    }
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                }
        })
    }

    fun setDataUserDetail(username: String, context: Context) {

        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token cb0060b3260e1505240db3dddcf3c526204d18e2")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)

                    val mUser = User()
                    mUser.avatar = responseObject.getString("avatar_url")
                    mUser.user = responseObject.getString("name")
                    mUser.username = responseObject.getString("login")
                    mUser.company = responseObject.getString("company")
                    mUser.location = responseObject.getString("location")
                    mUser.repository = responseObject.getString("public_repos")
                    mUser.followers  = responseObject.getString("followers")
                    mUser.following = responseObject.getString("following")

                    listItems.add(mUser)

                    listDataUser.postValue(listItems)
                } catch (e : Exception) {
                    Log.d("ExceptionDetail", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

        })
    }

    fun getDataUserSearch(username: String, context: Context) {

        val url = "https://api.github.com/search/users?q=$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token cb0060b3260e1505240db3dddcf3c526204d18e2")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    listItems.clear()
                    for (i in 0 until items.length()) {
                        val userGit = items.getJSONObject(i)
                        val usernameSearch = userGit.getString("login")
                        setDataUserDetail(usernameSearch, context)
                    }
                } catch (e: Exception){
                    Log.d("ExceptionSearch", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }

        })
    }


    fun getListUser() : LiveData<ArrayList<User>>{
        return listDataUser
    }
}



