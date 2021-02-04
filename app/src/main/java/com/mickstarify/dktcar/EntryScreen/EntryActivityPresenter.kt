package com.mickstarify.dktcar.EntryScreen

import android.content.Context
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers

class EntryActivityPresenter(val view: View) : Presenter {
    private val model: Model = EntryActivityModel(view as Context, this)

    init {
        Completable.fromAction {
            view.setQuestionModeToggle(model.getQuestionMode())
        }.subscribeOn(Schedulers.io()).subscribe()

    }

    override fun startTestButtonPressed() {
        model.initiateStartTest()
    }

    override fun browseQuestionsButtonPressed() {
        model.initiateBrowseQuestions()
    }

    override fun questionModeToggled(selectedMode: String) {
        model.setQuestionMode(selectedMode)
    }
}