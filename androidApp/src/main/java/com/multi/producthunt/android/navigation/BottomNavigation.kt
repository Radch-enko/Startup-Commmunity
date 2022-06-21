package com.multi.producthunt.android.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator

class BottomNavigationScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        BottomNavigation()
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BottomNavigation() {
    TabNavigator(HomeTab) {
        Scaffold(
            topBar = {},
            content = { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = if (WindowInsets.isImeVisible) WindowInsets.ime
                                .asPaddingValues()
                                .calculateBottomPadding()
                            else innerPadding.calculateBottomPadding()
                        )
                ) {
                    CurrentTab()
                }
            },
            bottomBar = {
                NavigationBar(
                    modifier = Modifier.navigationBarsPadding()
                ) {
                    TabNavigationItem(tab = HomeTab)
                    TabNavigationItem(tab = TimelineTab)
                    TabNavigationItem(tab = DiscussionsTab)
                    TabNavigationItem(tab = TopicsTab)
                    TabNavigationItem(tab = ProfileTab)
                }
            },
            modifier = Modifier
        )
    }
}