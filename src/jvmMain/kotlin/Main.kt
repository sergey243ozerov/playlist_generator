import androidx.compose.material.MaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.ApplicationScope
import kotlinx.coroutines.runBlocking

@Composable
@Preview
fun App(onButtonClick: () -> Unit) {
    var text by remember { mutableStateOf("Hello, World!") }

    MaterialTheme {
        Button(onClick = onButtonClick) {
            Text(text)
        }
    }
}

fun main() = application {
    val playlistSource = ApplicationScope.playlistsNetworkDataSource
    val authRepository = ApplicationScope.authRepository
    Window(onCloseRequest = ::exitApplication) {
        App{
            runBlocking {
                authRepository.authorize()
                playlistSource.getTopPlaylists()
            }
        }
    }
}
