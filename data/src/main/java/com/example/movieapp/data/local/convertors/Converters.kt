package com.example.movieapp.data.local.convertors

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.movieapp.data.local.entity.ProductionStatusEntity
import com.example.movieapp.data.local.entity.TypeEntity
import com.example.movieapp.domain.model.MovieCategory

class Converters {
    @TypeConverter
    fun fromStringList(list: List<String>?): String?{
        return list?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(data: String?): List<String>?{
        return data?.split(",")?.filter { it.isNotBlank() }
    }

    @TypeConverter
    fun fromProductionStatus(status: ProductionStatusEntity?): String?{
        return status?.name
    }

    @TypeConverter
    fun toProductionStatus(status: String?): ProductionStatusEntity?{
        return status?.let { ProductionStatusEntity.valueOf(it) }
    }

    @TypeConverter
    fun fromType(type: TypeEntity?): String?{
        return type?.name
    }

    @TypeConverter
    fun toType(type: String?): TypeEntity?{
        return type?.let { TypeEntity.valueOf(it) }
    }

    @TypeConverter
    fun fromMovieCategory(status: MovieCategory?): String?{
        return status?.name
    }

    @TypeConverter
    fun toMovieCategory(status: String?): MovieCategory?{
        return status?.let { MovieCategory.valueOf(it) }
    }
}

//private val json = Json { ignoreUnknownKeys = true }
//
//@TypeConverter
//fun fromStringList(list: List<String>?): String? =
//    list?.let { json.encodeToString(it) }
//
//@TypeConverter
//fun toStringList(jsonStr: String?): List<String>? =
//    jsonStr?.let { json.decodeFromString(it) }
//
//// Enum'ы можно оставить через .name — это безопасно и эффективно
//@TypeConverter
//fun fromProductionStatus(status: ProductionStatusEntity?): String? =
//    status?.name
//
//@TypeConverter
//fun toProductionStatus(name: String?): ProductionStatusEntity? =
//    name?.let { ProductionStatusEntity.valueOf(it) }
//
//@TypeConverter
//fun fromType(type: TypeEntity?): String? =
//    type?.name
//
//@TypeConverter
//fun toType(name: String?): TypeEntity? =
//    name?.let { TypeEntity.valueOf(it) }