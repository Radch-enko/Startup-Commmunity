package com.multi.producthunt.android.screen.addproject

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.statusBarsPadding
import com.multi.producthunt.MR
import com.multi.producthunt.android.R
import com.multi.producthunt.android.ui.*
import com.multi.producthunt.ui.models.SelectableTopicUI

class AddProjectScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<AddProjectViewModel>()
        val state = viewModel.state.collectAsState().value

        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp)
        ) {
            when {
                state.isLoading -> {
                    ProgressBar()
                }
                else -> {
                    AddProjectForm(
                        name = state.name,
                        tagline = state.tagline,
                        description = state.description,
                        thumbnail = state.thumbnail,
                        media = state.media,
                        topics = state.topics,
                        handleEvent = viewModel::sendEvent
                    )
                }
            }

            state.error?.let { error ->
                ErrorDialog(
                    error = error,
                    dismissError = {
                        viewModel.sendEvent(
                            AddProjectViewModel.Event.ErrorDismissed
                        )
                    }
                )
            }
        }
    }

    @Composable
    fun AddProjectForm(
        name: String,
        tagline: String,
        description: String,
        thumbnail: Uri?,
        media: List<Uri>,
        topics: List<SelectableTopicUI>,
        handleEvent: (event: AddProjectViewModel.Event) -> Unit,
    ) {
        val context = LocalContext.current
        val launcherForProjectAvatarImage = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let {
                handleEvent(AddProjectViewModel.Event.ThumnailChanged(uri))
            }
        }

        val launcherMultipleImages = rememberLauncherForActivityResult(
            contract =
            ActivityResultContracts.GetMultipleContents()
        ) { list ->
            list.forEach { uri ->
                handleEvent(AddProjectViewModel.Event.MediaChanged(uri))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {

            AddProjectTitle(modifier = Modifier.align(CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))

            PickProjectAvatar(
                modifier = Modifier.align(CenterHorizontally),
                image = thumbnail
            ) { launcherForProjectAvatarImage.launch("image/*") }


            OutlinedTextFieldDefault(
                value = name,
                onValueChange = { handleEvent(AddProjectViewModel.Event.NameChanged(it)) },
                label = stringResource(
                    id = MR.strings.project_name.resourceId
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldDefault(
                value = tagline,
                onValueChange = { handleEvent(AddProjectViewModel.Event.TaglineChanged(it)) },
                label = stringResource(
                    id = MR.strings.project_tagline.resourceId
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextFieldDefault(
                value = description,
                onValueChange = { handleEvent(AddProjectViewModel.Event.DescriptionChanged(it)) },
                label = stringResource(
                    id = MR.strings.project_description.resourceId
                ),
                singleLine = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            TitleMedium(text = stringResource(id = MR.strings.project_topics.resourceId))

            Spacer(modifier = Modifier.height(16.dp))

            TopicsSelector(
                topics,
                onToggle = { handleEvent(AddProjectViewModel.Event.TopicChanged(it)) })

            MediasPicker(media, onAddImages = {
                launcherMultipleImages.launch("image/*")
            }, onDelete = {
                handleEvent(AddProjectViewModel.Event.MediaChanged(it))
            })
        }
    }

    @Composable
    fun MediasPicker(
        mediasList: List<Uri>,
        onAddImages: () -> Unit,
        onDelete: (uri: Uri) -> Unit
    ) {
        Column {

            Spacer(modifier = Modifier.height(16.dp))

            ButtonDefault(
                text = stringResource(id = MR.strings.upload_medias.resourceId),
                onClick = onAddImages
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                itemsIndexed(mediasList) { index, item ->
                    MediaPickerImage(item, onDelete = onDelete)
                }
            }
        }
    }

    @Composable
    fun MediaPickerImage(image: Uri, onDelete: (uri: Uri) -> Unit) {
        Surface(
            modifier = Modifier
                .size(70.dp)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .border(
                    BorderStroke(
                        width = 3.dp,
                        color = MaterialTheme.colorScheme.primary,
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Image(
                bitmap = image.toImageBitmap(LocalContext.current), contentDescription = null,
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = { onDelete(image) }) {
                Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.White)
            }
        }
    }

    @Composable
    fun PickProjectAvatar(modifier: Modifier, image: Uri?, onClick: () -> Unit) {
        val commonModifier = modifier
            .size(100.dp)
            .clip(CircleShape)
            .border(2.dp, Color.DarkGray, CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                onClick()
            }

        if (image != null) {
            Image(
                bitmap = image.toImageBitmap(LocalContext.current),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = commonModifier,
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.no_image_icon),
                contentDescription = null,
                contentScale = ContentScale.Inside,
                modifier = commonModifier,
            )
        }
    }

    @Composable
    fun TopicsSelector(
        topics: List<SelectableTopicUI>,
        onToggle: (topicUI: SelectableTopicUI) -> Unit
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(topics) { topicUI ->
                var selected by remember {
                    mutableStateOf(topicUI.selected)
                }
                CustomChip(selected = selected, text = topicUI.title, onToggle = {
                    onToggle(topicUI)
                    selected = !selected
                })
            }
        }
    }

    @Composable
    fun AddProjectTitle(modifier: Modifier) {
        Text(
            text = stringResource(
                MR.strings.add_project.resourceId
            ), fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = modifier
        )
    }
}