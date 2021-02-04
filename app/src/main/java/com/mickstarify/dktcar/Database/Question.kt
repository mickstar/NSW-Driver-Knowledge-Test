package com.mickstarify.dktcar.Database

data class Question (
    val id: Int,
    val title: String,
    val category: String,
    val question: String,
    val picture: String,
    val optionA: String, // option A is always the correct option.
    val optionB: String,
    val optionC: String){

    fun hasPicture() : Boolean {
        return picture != "None"
    }

}
