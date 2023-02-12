package domain

import kotlinx.coroutines.*
import model.Track
import network.AuthRepository
import network.PlaylistsNetworkDataSource
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds
import java.util.Random as JavaRandom

class PlaylistGenerator(
    private val playlistsNetworkDataSource: PlaylistsNetworkDataSource,
    private val authRepository: AuthRepository
) {

    val random = Random(System.currentTimeMillis())
    val javaRandom = JavaRandom(System.currentTimeMillis())
    suspend fun generate(duration: Int): List<Pair<Track, Int>> {
        val loadedTracks = withContext(Dispatchers.IO) {
            authRepository.authorize()
            val playlistIds = playlistsNetworkDataSource.getTopPlaylists().shuffled(random)
            playlistIds.take(random.nextInt(1..2)).map {
                async { playlistsNetworkDataSource.getPlaylistTracks(it.id) }
            }.awaitAll().flatten()
        }
        val preparedTracks = loadedTracks.shuffled(random).take(random.nextInt(40..60)).sortedBy { it.durationMS }
        val desiredDurationMillis = duration.hours.inWholeMilliseconds
        val onePlayDuration = preparedTracks.sumOf { it.durationMS }
        val playsCount = desiredDurationMillis / onePlayDuration
        val tracksWithCount = preparedTracks.map { it to randomCount(playsCount) }.toMutableList()
        val resultDuration = tracksWithCount.sumOf { (track, count) -> track.durationMS * count }
        println("Desired duration = ${desiredDurationMillis.formatDuration()}, generated duration = ${resultDuration.formatDuration()}")
        if (resultDuration < desiredDurationMillis) {
            var tmpDuration = resultDuration
            while (tmpDuration < desiredDurationMillis) {
                val trackIndex = random.nextInt(0 until tracksWithCount.size)
                val (track, count) = tracksWithCount[trackIndex]
                tracksWithCount[trackIndex] = track to count + 1
                tmpDuration += track.durationMS
            }
        } else {
            var tmpDuration = resultDuration
            while (tmpDuration > desiredDurationMillis) {
                val trackIndex = random.nextInt(0 until tracksWithCount.size)
                val (track, count) = tracksWithCount[trackIndex]
                tracksWithCount[trackIndex] = track to count - 1
                tmpDuration -= track.durationMS
            }
        }
        val resultDuration2 = tracksWithCount.sumOf { (track, count) -> track.durationMS * count }
        println("Desired duration = ${desiredDurationMillis.formatDuration()}, generated duration = ${resultDuration2.formatDuration()}")
        return tracksWithCount.toList()
    }

    fun randomCount(mean: Long) =
        (javaRandom.nextGaussian(mean.toDouble(), sqrt(mean.toDouble()))).roundToInt().absoluteValue

    fun Long.formatDuration(): String = milliseconds.toComponents { hours, minutes, seconds, nanoseconds ->
        "%02d:%02d:%02d:%02d".format(hours, minutes, seconds, nanoseconds)
    }
}