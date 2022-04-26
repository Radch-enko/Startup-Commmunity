package com.multi.producthunt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.android.navigation.HomeTab
import com.multi.producthunt.android.navigation.SettingsTab
import com.multi.producthunt.android.navigation.TabNavigationItem
import com.multi.producthunt.android.navigation.TimelineTab
import com.multi.producthunt.android.ui.theme.ProductHuntMaterial2
import com.multi.producthunt.android.ui.theme.ProductHuntMaterial3
import com.multi.producthunt.android.ui.theme.white
import com.multi.producthunt.android.ui.theme.zircon

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val systemUiController = rememberSystemUiController()

            SideEffect {
                systemUiController.setNavigationBarColor(
                    color = zircon,
                    darkIcons = true
                )
                systemUiController.setStatusBarColor(
                    color = white,
                    darkIcons = true
                )
            }

            ProductHuntMaterial3 {
                ProductHuntMaterial2 {
                    TabNavigator(HomeTab) {
                        Scaffold(
                            topBar = {

                            },
                            content = { innerPadding ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(innerPadding)
                                ) {
                                    CurrentTab()
                                }
                            },
                            bottomBar = {
                                NavigationBar(containerColor = zircon, contentColor = zircon) {
                                    TabNavigationItem(tab = HomeTab)
                                    TabNavigationItem(tab = TimelineTab)
                                    TabNavigationItem(tab = SettingsTab)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
