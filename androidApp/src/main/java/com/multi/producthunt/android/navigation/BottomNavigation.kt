package com.multi.producthunt.android.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsPadding
import com.multi.producthunt.android.ui.theme.zircon

class BottomNavigationScreen: AndroidScreen(){
    @Composable
    override fun Content() {
        BottomNavigation()
    }
}

@Composable
fun BottomNavigation() {
    TabNavigator(HomeTab) {
        Scaffold(
            topBar = {

            },
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding()
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                NavigationBar(
                    containerColor = zircon,
                    contentColor = zircon,
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    TabNavigationItem(tab = HomeTab)
                    TabNavigationItem(tab = TimelineTab)
                    TabNavigationItem(tab = ProfileTab)
                }
            }

        )
    }
}