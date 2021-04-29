package com.dicoding.picodiploma.githubuser_submission3.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.githubuser_submission3.model.Follower
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class FollowerViewModel : ViewModel() {
    private val listDataFollower = MutableLiveData<ArrayList<Follower>>()
    private lateinit var errorMessage : String
    val listItemsFollower = ArrayList<Follower>()

    fun setDataFollower(username: String, context: Context) {

        val url = "https://api.github.com/users/$username/followers"
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
                    listItemsFollower.clear()
                    for (i in 0 until responseObject.length()) {
                        val userGit = responseObject.getJSONObject(i)
                        val username = userGit.getString("login")
                        setDataFollowerDetail(username, context)
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

    fun setDataFollowerDetail(username: String, context: Context) {

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

                    val mFollower = Follower()
                    mFollower.id = responseObject.getString("id")
                    mFollower.type = responseObject.getString("type")
                    mFollower.avatar = responseObject.getString("avatar_url")
                    mFollower.username = responseObject.getString("login")

                    listItemsFollower.add(mFollower)

                    listDataFollower.postValue(listItemsFollower)
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

    fun getListFollower() : LiveData<ArrayList<Follower>> {
        return listDataFollower
    }
}
