package startup.community.android.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
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
import cafe.adriel.voyager.navigator.Navigator
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest
import startup.community.android.R
import startup.community.android.screen.addproject.AddProjectScreen
import startup.community.android.ui.*
import startup.community.android.ui.theme.shadow
import startup.community.shared.MR
import startup.community.shared.ui.models.UserUI
import java.io.InputStream


class ProfileScreen(
    private val id: Int = 0,
    private val onLogout: (navigator: Navigator?) -> Unit,
    private val onShowProjects: (id: Int, navigator: Navigator?) -> Unit
) : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel: ProfileScreenViewModel = rememberScreenModel(arg = id)
        val state by viewModel.state.collectAsState()
        val systemUiController = rememberSystemUiController()
        val navigator = LocalNavigator.current
        val statusBarColor = MaterialTheme.colorScheme.surface

        LifecycleEffect(
            onStarted = {
                systemUiController.setStatusBarColor(Color.Transparent, darkIcons = false)
            },
            onDisposed = {

                systemUiController.setStatusBarColor(
                    color = statusBarColor,
                    darkIcons = true
                )
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
                    },
                    (state as ProfileScreenViewModel.State.Data).isEditable,
                    onUserProjects = {
                        viewModel.sendEvent(ProfileScreenViewModel.Event.ViewUserProject)
                    }
                )
            }
            is ProfileScreenViewModel.State.Error -> ErrorDialog(error = (state as ProfileScreenViewModel.State.Error).message) {
                viewModel.sendEvent(ProfileScreenViewModel.Event.ErrorDismissed)
            }
            ProfileScreenViewModel.State.Loading -> ProgressBar()
        }

        LaunchedEffect(key1 = null, block = {
            viewModel.effect.collectLatest { effect ->
                when (effect) {
                    ProfileScreenViewModel.Effect.SuccessLogout -> {
                        onLogout(navigator)
                    }
                    is ProfileScreenViewModel.Effect.ShowUserProjects -> {
                        onShowProjects(effect.id, navigator)
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
        editable: Boolean,
        onUserProjects: () -> Unit,
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
                    onLogout,
                    editable,
                    onUserProjects
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
        onLogout: () -> Unit,
        editable: Boolean,
        onUserProjects: () -> Unit
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

                        OutlinedButtonDefault(
                            text = stringResource(id = MR.strings.user_projects.resourceId),
                            onClick = onUserProjects
                        )

                        if (editable) {
                            Spacer(modifier = Modifier.height(16.dp))
                            val navigator = LocalNavigator.current?.parent
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
                    }

                    if (editable) {
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
        val navigator = LocalNavigator.current
        Surface(modifier = Modifier.fillMaxSize(), color = shadow) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (id != 0) {
                    BackButton(
                        onBack = { navigator?.pop() }, modifier = Modifier
                            .statusBarsPadding()
                            .padding(16.dp)
                    )
                }
                AnimatedVisibility(isRedact) {
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

        Box(modifier = modifier) {
            LoadableImage(
                link = profileImage,
                modifier = modifier
                    .size(120.dp)
                    .border(shape = CircleShape, width = 3.dp, color = White)
                    .clip(CircleShape),
                errorDrawable = R.drawable.no_profile_image
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