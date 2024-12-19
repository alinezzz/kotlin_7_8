package com.example.kotlin_pr8

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.kotlin_pr8.databinding.ActivityMainBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.textViewStatus.visibility = View.INVISIBLE;
        setContentView(binding.root)
        binding.button.setOnClickListener {
            val image_url = binding.editTextText.text.toString()
            if(image_url.isNotEmpty()){
                setDownloadImage(image_url)
            }
            else{
                val image_url_df = "https://avatars.mds.yandex.net/get-ott/2419418/2a0000017e12789f4466ce9f822f9ce7e15c/orig"
                setDownloadImage(image_url_df)
            }
        }
    }
    fun downloadImageByURl(imageUrl: String): Bitmap? {
        return try {
            val inputStream = URL(imageUrl).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            Log.e("Network", "Ошибка при скачивании")
            return null
        }
    }
    fun saveImageToDisk(bitmap: Bitmap) {
        val file = File(getOutputDirectory(), "image.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        Log.i("Disk","Фото сохранено: ${file.absolutePath}")
    }
    fun getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(this.filesDir, "photos").apply { mkdirs() }
        }
        return mediaDir ?: this.filesDir
    }
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    fun setDownloadImage(image_url : String){
        val NetworkTread = newSingleThreadContext("Network")
        val DiskTead = newSingleThreadContext("Disk")
        GlobalScope.launch(NetworkTread) {
            val bitmap = downloadImageByURl(image_url)
            if (bitmap == null){
                launch(Dispatchers.Main) {
                    binding.imageView.visibility = View.GONE
                    binding.textViewStatus.visibility = View.VISIBLE
                }
            }
            else {
                Log.i("Network", "Изображение скачано")

                launch(Dispatchers.Main) {
                    binding.imageView.setImageBitmap(bitmap)
                    binding.imageView.visibility = View.VISIBLE
                    binding.textViewStatus.visibility = View.GONE
                }.join()
                launch(DiskTead) {
                    saveImageToDisk(bitmap)
                }
            }
        }
    }
}