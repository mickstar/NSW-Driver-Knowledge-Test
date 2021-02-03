package com.mickstarify.nswdriverknowledgetest.EntryScreen

import android.content.Context
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class EntryActivityPresenter(val view: View) : Presenter {
    private val model: Model = EntryActivityModel(view as Context, this)

    init {
        view.initUI()

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