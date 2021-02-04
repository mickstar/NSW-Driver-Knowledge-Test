package com.mickstarify.dktcar.QuestionListScreen

import androidx.recyclerview.widget.RecyclerView

interface View {
    fun initUI()
    fun populateList(questionListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?)
    fun pressedQuestion(questionTitle: String)
}

interface Presenter {
}

interface Model {
    fun getQuestions(): List<QuestionCategoryObject>
    fun openQuestionWithId(questionId: Int)
}