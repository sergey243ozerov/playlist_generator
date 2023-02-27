package domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Track
import model.TrackDataScheme
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import kotlin.time.Duration.Companion.milliseconds

object DataSaver {

    suspend fun saveData(path: String, generatedData: List<Track>) {
        withContext(Dispatchers.IO) {
            val dataFrame = generatedData.mapIndexed { index, track ->
                TrackDataScheme(
                    index = index,
                    authors = track.artists,
                    songName = track.name,
                    duration = track.duration.milliseconds.toComponents { _, minutes, seconds, _ ->
                        "%02d:%02d".format(minutes, seconds)
                    },
                    times = track.count
                )
            }.toDataFrame()
            dataFrame.writeExcel(path)
        }
    }
}