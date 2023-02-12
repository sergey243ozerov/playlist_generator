import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.ApplicationScope
import view.model.*
import java.awt.FileDialog as AwtFileDialog

@Composable
@Preview
fun DurationInputScreen(input: String, onInputChanged: (String) -> Unit, onGenerateClicked: () -> Unit) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = input, onValueChange = onInputChanged)
        Button(onClick = { onGenerateClicked() }) {
            Text("Сгенерировать")
        }
    }
}

@Composable
@Preview
fun ChooseFileScreen(path: String, onSelectFileClicked: () -> Unit, onConfirmClicked: () -> Unit) {
    val showConfirm by derivedStateOf { path.isNotEmpty() }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (path.isEmpty()) Text("Выберите путь") else Text("Выбранный путь: $path")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onSelectFileClicked) {
                Text("Выбрать файл")
            }
            if (showConfirm) {
                Button(onConfirmClicked) {
                    Text("Сохранить")
                }
            }
        }
    }
}

@Composable
@Preview
fun DoneScreen(onNavigateClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Файл успешно сгенерирован")
        Button(onClick = onNavigateClicked) {
            Text("Перейти")
        }
    }
}

@Composable
@Preview
fun LoadingScreen() {
    Box(Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

fun main() = application {
    val viewModel = ApplicationScope.viewModel
    val state by viewModel.state.collectAsState()
    Window(onCloseRequest = ::exitApplication) {
        val dialog = remember { AwtFileDialog(window) }
        dialog.mode = AwtFileDialog.SAVE
        MaterialTheme {
            when (val localState = state) {
                is InputDuration -> {
                    DurationInputScreen(
                        input = localState.durationInput,
                        onInputChanged = { viewModel.onDurationInput(it) },
                        onGenerateClicked = { viewModel.generate() }
                    )
                }

                is ChooseFile -> {
                    dialog.isVisible = false
                    ChooseFileScreen(
                        path = localState.path,
                        onSelectFileClicked = { viewModel.onSelectFileClicked() },
                        onConfirmClicked = {
                            viewModel.onConfirmClicked()
                        }
                    )
                }

                is Done -> DoneScreen {
                    viewModel.onNavigateClicked()
                    exitApplication()
                }
                is FileDialog -> {
                    dialog.isVisible = true
                    viewModel.onFileSelected(dialog.file, dialog.directory)
                }
                Loading -> LoadingScreen()
            }
        }
    }
}
