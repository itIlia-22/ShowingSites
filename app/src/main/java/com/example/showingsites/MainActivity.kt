package com.example.showingsites

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.example.showingsites.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearch.setOnClickListener(clickListener)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    var clickListener: View.OnClickListener = View.OnClickListener {
        try {
            val urlText = binding.url.text.toString()
            val uri = URL(urlText)


                var urlConnection: HttpsURLConnection? = null
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    Thread {
                    val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val result = getLines(reader)
                        runOnUiThread{
                            binding.webView.loadDataWithBaseURL(null,
                                result,
                                "text/html; utf-8",
                                "utf-8",
                                null)
                        }

                    }.start()
                } catch (e: Exception) {
                    Log.e("1", "Fail connection", e)
                    e.printStackTrace()
                } finally {
                    urlConnection?.disconnect()
                }

        } catch (e: MalformedURLException) {
            Log.e("1", "Fail URI", e)
            e.printStackTrace()
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}