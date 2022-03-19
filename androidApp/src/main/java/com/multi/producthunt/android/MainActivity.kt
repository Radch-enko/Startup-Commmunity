package com.multi.producthunt.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Scaffold
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.multi.producthunt.android.navigation.HomeTab
import com.multi.producthunt.android.navigation.SettingsTab
import com.multi.producthunt.android.navigation.TabNavigationItem
import com.multi.producthunt.android.navigation.TimelineTab
import com.multi.producthunt.android.ui.theme.ProductHuntTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProductHuntTheme() {
                TabNavigator(HomeTab) {
                    Scaffold(
                        content = {
                            CurrentTab()
                        },
                        bottomBar = {
                            BottomNavigation {
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
