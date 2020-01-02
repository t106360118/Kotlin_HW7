package com.example.kotlin_hw7_lab12

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var receiver=object :BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            var data:Data=Gson().fromJson(intent!!.extras!!.getString("json"),Data::class.java)
            var items:Array<String?> = arrayOfNulls(data.result!!.results.size)
            for (i in 0 until items.size)
            {
                items[i] =
                    "\n列車即將進入 :" + data.result!!.results[i].Station + "\n列車行駛目的地 :" + data.result!!.results[i].Destination
            }
            runOnUiThread {
                AlertDialog.Builder(this@MainActivity).setTitle("台北捷運列車到站站名").setItems(items, null).show()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(receiver, IntentFilter("MyMessage"))
        btn.setOnClickListener {
            var req:Request=Request.Builder().url("https://data.taipei/opendata/datalist/apiAccess?scope=resourceAquire&rid=55ec6d6e-dc5c-4268-a725-d04cc262172b").build()
            OkHttpClient().newCall(req).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("查詢失敗", e.toString())
                }
                override fun onResponse(call: Call, response: Response) {
                    sendBroadcast(Intent("MyMessage").putExtra("json",response.body!!.string()))
                }
            })
        }
    }
    override fun onDestroy()
    {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
internal class Data {
    var result: Result? = null
    internal inner class Result {
        lateinit var results :Array<Results>
        internal inner class Results {
            var Station: String? = null
            var Destination: String? = null
        }
    }
}
