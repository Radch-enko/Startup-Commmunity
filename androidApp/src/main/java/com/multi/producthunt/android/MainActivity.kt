package com.multi.producthunt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.SideEffect
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.android.navigation.HomeTab
import com.multi.producthunt.android.navigation.SettingsTab
import com.multi.producthunt.android.navigation.TabNavigationItem
import com.multi.producthunt.android.navigation.TimelineTab
import com.multi.producthunt.android.ui.theme.ProductHuntTheme
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

            ProductHuntTheme() {
                TabNavigator(HomeTab) {
                    Scaffold(
                        topBar = {

                        },
                        content = {
                            CurrentTab()
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
