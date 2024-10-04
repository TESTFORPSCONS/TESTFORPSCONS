package com.example.testarcanitonefile


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.example.testarcanitonefile.ui.AppNavigation
import com.example.testarcanitonefile.di.networkModule
import com.example.testarcanitonefile.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (org.koin.core.context.GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainActivity)
                modules(listOf(networkModule, viewModelModule))
            }
        }

        setContent {
            MaterialTheme {
                Surface(color = MaterialTheme.colors.background) {
                    AppNavigation()
                }
            }
        }
    }
}

