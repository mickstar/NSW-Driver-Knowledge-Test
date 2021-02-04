package com.mickstarify.dktcar.QuestionListScreen

import android.content.Context
import androidx.recyclerview.widget.RecyclerView

class QuestionListActivityPresenter(context: Context, val view: View) : Presenter {
    val model = QuestionListActivityModel(context)

    init {
        view.initUI()
        view.populateList(getQuestionListAdapter())
    }

    fun getQuestionListAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>? {
        return QuestionListAdapter(model.context, model.getQuestions(), model.preferenceManager.getQuestionMode(), object: QuestionListAdapterListener{
            override fun onQuestionPressed(questionId: Int) {
                model.openQuestionWithId(questionId)
            }

        })
    }
}