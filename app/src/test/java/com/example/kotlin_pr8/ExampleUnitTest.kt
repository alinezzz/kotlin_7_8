package com.example.kotlin_pr8

import android.graphics.BitmapFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class MainActivityUnitTest {
    private lateinit var activity: MainActivity
    @Before
    fun setUp(){
        activity = Robolectric.buildActivity(MainActivity::class.java).create().get()
    }

    @Test
    fun downloadingUser_isCorrect() {
        val image_url= "https://avatars.mds.yandex.net/get-kinopoisk-image/1600647/52548a69-37d6-44e7-829f-65f0eb9facfc/1920x"
        Assert.assertEquals(true,activity.downloadImageByURl(image_url)!=null )
    }
    @Test
    fun downloadingUser_isIncorrect() {
        val image_url_incorrect = "rrrrrr"
        Assert.assertEquals(false,activity.downloadImageByURl(image_url_incorrect)!=null )
    }
    @Test
    fun saving_isCorrect() {
        val bitmap = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_launcher_background)
        activity.saveImageToDisk(bitmap)
        val logMessageGood = (ShadowLog.getLogsForTag("Disk").lastOrNull())?.msg?.contains("Фото сохранено")
        Assert.assertEquals(true, logMessageGood)
    }
    @Test
    fun setDownloadImageTest_Correct() = runBlocking{
        val image_url= "https://avatars.mds.yandex.net/get-kinopoisk-image/1600647/52548a69-37d6-44e7-829f-65f0eb9facfc/1920x"
        activity.setDownloadImage(image_url)
        delay(1000)
        val logMessageGood = (ShadowLog.getLogsForTag("Network").lastOrNull())?.msg?.contains("Изображение скачано")
        Assert.assertEquals(true, logMessageGood)
    }
    @Test
    fun setDownloadImageTest_inCorrect() = runBlocking{
        val image_url= " "
        activity.setDownloadImage(image_url)
        delay(1000)
        val logMessageGood = (ShadowLog.getLogsForTag("Network").lastOrNull())?.msg?.contains("Изображение скачано")
        Assert.assertEquals(false, logMessageGood)
    }



}