package com.mickstarify.dktcar

import android.content.Context
import androidx.core.content.edit
import com.mickstarify.dktcar.Database.QuestionDatabaseMode

class PreferenceManager(val context: Context) {
    val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    fun getQuestionMode(): QuestionDatabaseMode {
        return when(sharedPreferences.getString("QUESTION_MODE", "unset")){
            "RIDER" -> QuestionDatabaseMode.RIDER
            "CAR" -> QuestionDatabaseMode.CAR
            "unset" -> QuestionDatabaseMode.CAR // default to Car. (This is required for the first launch where there is no setting)
            else -> throw Exception("Error invalid setting for question mode")
        }
    }

    fun setQuestionMode(mode: QuestionDatabaseMode) {
        val setting = when (mode){
            QuestionDatabaseMode.CAR -> "CAR"
            QuestionDatabaseMode.RIDER -> "RIDER"
        }

        sharedPreferences.edit() { putString("QUESTION_MODE", setting)}
    }


}