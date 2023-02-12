package domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.Track
import model.TrackDataScheme
import org.jetbrains.kotlinx.dataframe.api.toDataFrame
import org.jetbrains.kotlinx.dataframe.io.writeExcel
import kotlin.time.Duration.Companion.milliseconds

object DataSaver {

    suspend fun saveData(path: String, generatedData: List<Pair<Track, Int>>) {
        withContext(Dispatchers.IO) {
            val dataFrame = generatedData.mapIndexed { index, (track, times) ->
                TrackDataScheme(
                    index = index,
                    authors = track.artists.joinToString { it.name },
                    songName = track.name,
                    duration = track.durationMS.milliseconds.toComponents { _, minutes, seconds, _ ->
                        "%02d:%02d".format(minutes, seconds)
                    },
                    times = times
                )
            }.toDataFrame()
            dataFrame.writeExcel(path)
        }
    }
}