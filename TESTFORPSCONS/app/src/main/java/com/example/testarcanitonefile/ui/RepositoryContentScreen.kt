package com.example.testarcanitonefile.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.testarcanitonefile.viewmodels.RepositoryContentViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RepositoryContentScreen(owner: String, repo: String, navController: NavHostController) {
    val viewModel: RepositoryContentViewModel = koinViewModel()

    LaunchedEffect(viewModel.currentPath) {
        viewModel.loadContent(owner, repo, viewModel.currentPath)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = if (viewModel.currentPath.isEmpty()) repo else viewModel.currentPath)
            },
            navigationIcon = {
                IconButton(onClick = {
                    if (viewModel.currentPath.isEmpty()) {
                        navController.popBackStack()
                    } else {
                        val lastSlashIndex = viewModel.currentPath.lastIndexOf('/')
                        viewModel.currentPath = if (lastSlashIndex != -1) {
                            viewModel.currentPath.substring(0, lastSlashIndex)
                        } else {
                            ""
                        }
                        viewModel.loadContent(owner, repo, viewModel.currentPath)
                    }
                }) {
                    Icon(
                        androidx.compose.material.icons.Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    viewModel.currentPath = ""
                    viewModel.loadContent(owner, repo, viewModel.currentPath)
                }) {
                    Icon(
                        androidx.compose.material.icons.Icons.Default.Home,
                        contentDescription = "Home"
                    )
                }
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (viewModel.isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                viewModel.errorMessage.value?.let { error ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = error,
                            color = androidx.compose.ui.graphics.Color.Red,
                            modifier = Modifier.padding(16.dp)
                        )
                        Button(onClick = { viewModel.loadContent(owner, repo, viewModel.currentPath) }) {
                            Text("Повторить")
                        }
                    }
                } ?: run {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(viewModel.contentList.value) { content ->
                            RepositoryContentItem(
                                content = content,
                                onClick = {
                                    if (content.type == "file") {
                                        val encodedUrl = Uri.encode(content.download_url)
                                        navController.navigate(route = "webview/$encodedUrl")
                                    } else {
                                        viewModel.currentPath =
                                            if (viewModel.currentPath.isEmpty()) content.name else "${viewModel.currentPath}/${content.name}"
                                        viewModel.loadContent(owner, repo, viewModel.currentPath)
                                    }
                                }
                            )
                            Divider(
                                color = androidx.compose.ui.graphics.Color.Gray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}
