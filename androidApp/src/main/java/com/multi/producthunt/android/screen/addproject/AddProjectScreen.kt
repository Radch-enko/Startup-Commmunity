package com.multi.producthunt.android.screen.addproject

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.statusBarsPadding
import com.multi.producthunt.MR
import com.multi.producthunt.android.ui.*
import com.multi.producthunt.ui.models.SelectableTopicUI

class AddProjectScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        val viewModel = rememberScreenModel<AddProjectViewModel>()
        val state by viewModel.state.collectAsState()

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
        thumbnail: ByteArray?,
        media: List<ByteArray> = emptyList(),
        topics: List<SelectableTopicUI> = emptyList(),
        handleEvent: (event: AddProjectViewModel.Event) -> Unit
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            AddProjectTitle(modifier = Modifier.align(CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))


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
                CustomChip(selected = topicUI.selected, text = topicUI.title, onToggle = { onToggle(topicUI) })
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