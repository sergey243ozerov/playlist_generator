package model

import org.jetbrains.kotlinx.dataframe.annotations.ColumnName
import org.jetbrains.kotlinx.dataframe.annotations.DataSchema
import org.jetbrains.kotlinx.dataframe.schema.DataFrameSchema

data class TrackDataScheme(
    @ColumnName("№")
    val index: Int,
    @ColumnName("Исполнитель")
    val authors: String,
    @ColumnName("Название")
    val songName: String,
    @ColumnName("Длительность")
    val duration : String,
    @ColumnName("Количество")
    val times: Int
)