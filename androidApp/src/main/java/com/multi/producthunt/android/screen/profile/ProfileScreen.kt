package com.multi.producthunt.android.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.multi.producthunt.MR
import com.multi.producthunt.android.navigation.HomeTab
import com.multi.producthunt.android.screen.addproject.AddProjectScreen
import com.multi.producthunt.android.ui.ButtonDefault
import com.multi.producthunt.android.ui.ErrorDialog
import com.multi.producthunt.android.ui.MediumText
import com.multi.producthunt.android.ui.OutlinedButtonDefault
import com.multi.producthunt.android.ui.ProgressBar
import com.multi.producthunt.android.ui.RedactButton
import com.multi.producthunt.android.ui.UpdateValueDialog
import com.multi.producthunt.android.ui.getImageLoader
import com.multi.producthunt.android.ui.placeholder
import com.multi.producthunt.android.ui.theme.shadow
import kotlinx.coroutines.flow.collectLatest
import java.io.InputStream


class ProfileScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<ProfileScreenViewModel>()
        val state by viewModel.state.collectAsState()

        val systemUiController = rememberSystemUiController()

        LifecycleEffect(
            onStarted = {
                systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
            },
            onDisposed = {
                systemUiController.setStatusBarColor(White, darkIcons = true)
            }
        )

        when (state) {
            is ProfileScreenViewModel.State.Data -> {
                UserProfile(
                    (state as ProfileScreenViewModel.State.Data).userUI,
                    onProfileUploaded = { byteArray: ByteArray ->
                        viewModel.sendEvent(
                            ProfileScreenViewModel.Event.PickProfileImage(byteArray)
                        )
                    }, onCoverUploaded = { byteArray: ByteArray ->
                        viewModel.sendEvent(
                            ProfileScreenViewModel.Event.PickCoverImage(byteArray)
                        )
                    },
                    onNameChanged = { newName: String ->
                        viewModel.sendEvent(ProfileScreenViewModel.Event.OnNameChanged(newName))
                    },
                    onHeadlineChanged = { newHeadline: String ->
                        viewModel.sendEvent(
                            ProfileScreenViewModel.Event.OnHeadlineChanged(
                                newHeadline
                            )
                        )
                    },
                    onLogout = {
                        viewModel.sendEvent(ProfileScreenViewModel.Event.Logout)
                        viewModel.onDispose()
                    }
                )
            }
            is ProfileScreenViewModel.State.Error -> ErrorDialog(error = (state as ProfileScreenViewModel.State.Error).message) {
                viewModel.sendEvent(ProfileScreenViewModel.Event.ErrorDismissed)
            }
            ProfileScreenViewModel.State.Loading -> ProgressBar()
        }

        val tabNavigator = LocalTabNavigator.current
        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    ProfileScreenViewModel.Effect.SuccessLogout -> {
                        tabNavigator.current = HomeTab
                    }
                }
            }
        })
    }

    @Composable
    fun UserProfile(
        userUI: UserUI,
        onProfileUploaded: (ByteArray) -> Unit,
        onCoverUploaded: (ByteArray) -> Unit,
        onNameChanged: (String) -> Unit,
        onHeadlineChanged: (String) -> Unit,
        onLogout: () -> Unit,
    ) {
        val context = LocalContext.current
        val launcherForProfileImage = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val inputStream: InputStream? =
                    context.contentResolver.openInputStream(uri)

                if (inputStream != null) {
                    onProfileUploaded(inputStream.readBytes())
                }
            }
        }

        val launcherForCoverImage = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                val inputStream: InputStream? =
                    context.contentResolver.openInputStream(uri)

                if (inputStream != null) {
                    onCoverUploaded(inputStream.readBytes())
                }
            }
        }

        var isRedact by remember {
            mutableStateOf(false)
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                ProfileCoverImage(
                    userUI.coverImage,
                    isRedact,
                    onCoverUploaded = { launcherForCoverImage.launch("image/*") })
                UserCard(
                    modifier = Modifier.align(Alignment.Center),
                    userUI = userUI,
                    isRedact,
                    onRedactClicked = { isRedact = !isRedact },
                    onUploadProfileImage = {
                        launcherForProfileImage.launch(
                            "image/*"
                        )
                    },
                    onNameChanged,
                    onHeadlineChanged,
                    onLogout
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun UserCard(
        modifier: Modifier,
        userUI: UserUI,
        isRedact: Boolean,
        onRedactClicked: () -> Unit,
        onUploadProfileImage: () -> Unit,
        onNameChanged: (String) -> Unit,
        onHeadlineChanged: (String) -> Unit,
        onLogout: () -> Unit
    ) {
        Box(modifier = modifier) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(.8f)
                    .padding(top = 50.dp)
            ) {
                Box {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 80.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = userUI.name, style = typography.headlineSmall)
                            var dialogVisibility by remember {
                                mutableStateOf(false)
                            }
                            UpdateValueDialog(
                                label = stringResource(id = MR.strings.enter_username.resourceId),
                                onDismissError = { dialogVisibility = !dialogVisibility },
                                onOkCallback = onNameChanged,
                                isVisible = dialogVisibility
                            )
                            RedactButton(isRedact) {
                                dialogVisibility = !dialogVisibility
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(text = userUI.username, style = typography.titleMedium)

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            userUI.headline?.let { MediumText(text = it) }
                            var dialogVisibility by remember {
                                mutableStateOf(false)
                            }
                            UpdateValueDialog(
                                label = stringResource(id = MR.strings.enter_headline.resourceId),
                                onDismissError = { dialogVisibility = !dialogVisibility },
                                onOkCallback = onHeadlineChanged,
                                isVisible = dialogVisibility
                            )
                            RedactButton(isRedact) {
                                dialogVisibility = !dialogVisibility
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        val navigator = LocalNavigator.current?.parent?.parent
                        OutlinedButtonDefault(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = MR.strings.add_project.resourceId),
                            onClick = {
                                navigator?.push(AddProjectScreen())
                            })

                        Spacer(modifier = Modifier.height(16.dp))

                        ButtonDefault(
                            text = stringResource(id = MR.strings.logout.resourceId),
                            onClick = onLogout
                        )
                    }

                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = onRedactClicked
                    ) {
                        Icon(
                            if (isRedact) Icons.Filled.Settings else Icons.Filled.ManageAccounts,
                            contentDescription = null
                        )
                    }
                }
            }
            ProfileImage(
                modifier = Modifier.align(Alignment.TopCenter),
                userUI.profileImage,
                isRedact,
                onUploadProfileImage
            )
        }
    }

    @Composable
    fun ProfileCoverImage(
        profileImage: String?,
        isRedact: Boolean,
        onCoverUploaded: () -> Unit
    ) {
        var placeholder by remember {
            mutableStateOf(true)
        }
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileImage)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .placeholder(placeholder),
                onLoading = {
                    placeholder = true
                },
                onSuccess = {
                    placeholder = false
                },
                imageLoader = getImageLoader(LocalContext.current)
            )
        }

        Surface(modifier = Modifier.fillMaxSize(), color = shadow) {
            AnimatedVisibility(isRedact) {
                Box(modifier = Modifier.fillMaxSize()) {
                    IconButton(
                        onClick = onCoverUploaded,
                        modifier = Modifier
                            .statusBarsPadding()
                            .padding(16.dp)
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(Icons.Filled.Wallpaper, contentDescription = null, tint = White)
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun ProfileImage(
        modifier: Modifier,
        profileImage: String?,
        isRedact: Boolean,
        onUploadProfileImage: () -> Unit
    ) {

        val cameraPermissionState = rememberMultiplePermissionsState(
            listOf(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
            )
        )

        var placeholder by remember {
            mutableStateOf(true)
        }
        Box(modifier = modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(profileImage)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = modifier
                    .size(120.dp)
                    .border(shape = CircleShape, width = 3.dp, color = White)
                    .clip(CircleShape)
                    .placeholder(placeholder),
                onLoading = {
                    placeholder = true
                },
                onSuccess = {
                    placeholder = false
                },
                imageLoader = getImageLoader(LocalContext.current),
            )

            AnimatedVisibility(isRedact, modifier = Modifier.align(Alignment.BottomEnd)) {
                IconButton(
                    onClick = {
                        if (cameraPermissionState.allPermissionsGranted) {
                            onUploadProfileImage()
                        } else {
                            cameraPermissionState.launchMultiplePermissionRequest()
                        }
                    }
                ) {
                    Icon(
                        Icons.Outlined.AddCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}