//package com.kickhead.registrationwithdb.database
//
//
//import androidx.room.TypeConverter
//import app.amigoz.android.model.exam.Option
//import app.amigoz.android.model.exam.Question
//import app.amigoz.android.model.exam.Section
//import app.amigoz.android.model.internshipquiz.InternshipQuizQuestionModelList
//import com.google.gson.Gson
//import org.json.JSONArray
//
//class Converters {
//
//
//    @TypeConverter
//    fun jsonArrayToString(value: JSONArray?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun stringToJsonArray(value: String?):JSONArray {
//        if(value!=null){
//            return JSONArray(Gson().toJson(value))
//        }else{
//            return JSONArray()
//        }
//    }
//
//
//    @TypeConverter
//    fun listToJson(value: List<InternshipQuizQuestionModelList>?): String {
//
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun jsonToList(value: String): List<InternshipQuizQuestionModelList>? {
//
//        val objects = Gson().fromJson(value, Array<InternshipQuizQuestionModelList>::class.java) as Array<InternshipQuizQuestionModelList>
//        val list = objects.toList()
//        return list
//    }
//
//
//    @TypeConverter
//    fun sectionListToJson(value: List<Section>?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun jsonToSectionList(value: String): List<Section>? {
//        val objects = Gson().fromJson(value, Array<Section>::class.java) as Array<Section>
//        return objects.toList()
//    }
//
//
//    @TypeConverter
//    fun questionListToJson(value: List<Question>?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun jsonToQuestionList(value: String): List<Question>? {
//        val objects = Gson().fromJson(value, Array<Question>::class.java) as Array<Question>
//        return objects.toList()
//    }
//
//    @TypeConverter
//    fun optionListToJson(value: List<Option>?): String {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun jsonToOptionList(value: String): List<Option>? {
//        val objects = Gson().fromJson(value, Array<Option>::class.java) as Array<Option>
//        return objects.toList()
//    }
//}
