package view.model

import model.Track

sealed interface ViewState

data class InputDuration(val durationInput: String): ViewState

object Loading: ViewState

data class ChooseFile(val generatedFiles: List<Track>, val path: String): ViewState

data class FileDialog(val generatedFiles: List<Track>): ViewState

data class Done(val path: String): ViewState