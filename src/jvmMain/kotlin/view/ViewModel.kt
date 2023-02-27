package view

import domain.DataSaver
import domain.GeneratorRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import view.model.*
import java.awt.Desktop
import java.io.File
import kotlin.coroutines.CoroutineContext

class ViewModel(private val generator: GeneratorRepository) : CoroutineScope {
    private val _state: MutableStateFlow<ViewState> = MutableStateFlow(InputDuration(""))
    val state: StateFlow<ViewState>
        get() = _state.asStateFlow()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    fun onDurationInput(text: String) {
        _state.value = InputDuration(text)
    }

    fun generate() {
        val localState = _state.value as InputDuration
        launch {
            _state.value = Loading
            val result = generator.getTracks(localState.durationInput.toInt())
            _state.value = ChooseFile(result,"")
        }
    }

    fun onSelectFileClicked() {
        val localState = _state.value as ChooseFile
        _state.value = FileDialog(localState.generatedFiles)
    }

    fun onConfirmClicked() {
        launch {
            val chooseFileState = _state.value as ChooseFile
            _state.value = Loading
            DataSaver.saveData(chooseFileState.path, chooseFileState.generatedFiles)
            _state.value = Done(chooseFileState.path)
        }
    }

    fun onNavigateClicked() {
        val localState = _state.value as Done
        Desktop.getDesktop().open(File(localState.path).parentFile)
    }

    fun onFileSelected(file: String?, directory: String?) {
        val chooseFileState = _state.value as FileDialog
        var finalName = file
        if (file?.endsWith(".xlsx") == false){
            finalName = "$file.xlsx"
        }
        _state.value = ChooseFile(chooseFileState.generatedFiles, directory.orEmpty() + finalName.orEmpty())
    }
}