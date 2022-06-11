package com.multi.producthunt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.android.navigation.BottomNavigationScreen
import com.multi.producthunt.android.ui.theme.ProductHuntMaterial2
import com.multi.producthunt.android.ui.theme.ProductHuntMaterial3

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()

            WindowCompat.setDecorFitsSystemWindows(window, false)

            ProductHuntMaterial3 {
                ProductHuntMaterial2 {
                    Navigator(screen = BottomNavigationScreen()) { navigator ->
                        SlideTransition(navigator)
                    }
                }
                val statusBarColor = MaterialTheme.colorScheme.surface

                systemUiController.setNavigationBarColor(
                    color = statusBarColor,
                    darkIcons = true
                )
                systemUiController.setStatusBarColor(
                    color = statusBarColor,
                    darkIcons = true
                )
            }
        }
    }
}
