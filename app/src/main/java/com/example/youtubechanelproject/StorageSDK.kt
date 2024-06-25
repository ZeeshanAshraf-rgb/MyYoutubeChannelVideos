package com.example.youtubechanelproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlin.collections.ArrayList


@SuppressLint("StaticFieldLeak")
object StorageSDK {
    private lateinit var context: Context
    private lateinit var sharedPreference: SharedPreferences
    fun init(context: Context) {
        this.context = context
        sharedPreference =  context.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
    }
    fun saveTimeStamp() {
        var editor = sharedPreference.edit()
        editor.putLong("timestamp", System.currentTimeMillis())
        editor.commit()
    }

    fun getTimeStamp():Long {
       return sharedPreference.getLong("timestamp",0)
    }

    fun getVideosData(): ArrayList<Video> {
        //  return sharedPreference.getString("video_data_key",0)
        var theList = arrayListOf<Video>()
       // val gson = Gson()
        val jsonText: String? = sharedPreference.getString("video_data_key", "")
      //  val text = gson.fromJson(jsonText, ArrayList<String>::class.java) //EDIT: gso to gson
        if(!jsonText.isNullOrEmpty()) {
            val gson = GsonBuilder().create()
            val theList = gson.fromJson<ArrayList<Video>>(jsonText, object :
                TypeToken<ArrayList<Video>>() {}.type)
        }

        return theList

    }

    fun saveVideosData(list: List<Video>) {
        val gson = Gson()
        //val textList: List<String> = ArrayList(data)
        val jsonText = gson.toJson(list)
        val editor = sharedPreference.edit()
        editor.putString("video_data_key", jsonText)
        editor.commit()
    }
}
