package com.loper7.datepicker

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 *@Author loper7
 *@Date 2021/9/15 16:20
 *@Description
 **/
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(color = Color.White) {
                Greeting()
            }
        }
    }


    @Composable
    fun Greeting() {
        Text(text = "Hello,Jetpack Compose!")
    }
}


