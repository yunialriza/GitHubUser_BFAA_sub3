package com.dicoding.picodiploma.consumerapp.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.consumerapp.model.Following
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowingViewModel : ViewModel() {

    private val listDataFollowing = MutableLiveData<ArrayList<Following>>()
    private lateinit var errorMessage : String
    val listItemsFollowing = ArrayList<Following>()

    fun setDataFollowing(username: String, context: Context) {

        val url = "https://api.github.com/users/$username/following"
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
                    listItemsFollowing.clear()
                    for (i in 0 until responseObject.length()) {
                        val userGit = responseObject.getJSONObject(i)
                        val username = userGit.getString("login")
                        setDataFollowingDetail(username, context)
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

    fun setDataFollowingDetail(username: String, context: Context) {

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

                    val mFollowing = Following()
                    mFollowing.id = responseObject.getString("id")
                    mFollowing.type = responseObject.getString("type")
                    mFollowing.avatar = responseObject.getString("avatar_url")
                    mFollowing.username = responseObject.getString("login")

                    listItemsFollowing.add(mFollowing)

                    listDataFollowing.postValue(listItemsFollowing)
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

    fun getListFollowing() : LiveData<ArrayList<Following>> {
        return listDataFollowing
    }
}