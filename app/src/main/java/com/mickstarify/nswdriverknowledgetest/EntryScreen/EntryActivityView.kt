package com.mickstarify.nswdriverknowledgetest.EntryScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.creageek.segmentedbutton.SegmentedButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.mickstarify.nswdriverknowledgetest.Database.QuestionDatabaseMode
import com.mickstarify.nswdriverknowledgetest.R

class EntryActivityView : AppCompatActivity(), View {
    lateinit var presenter: Presenter;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_view)
        presenter = EntryActivityPresenter(this)

    }

    override fun setQuestionModeToggle(questionMode: QuestionDatabaseMode) {
        val questionModeToggleGroup =
            findViewById<MaterialButtonToggleGroup>(R.id.toggleQuestionMode)

        val id = when(questionMode) {
            QuestionDatabaseMode.CAR -> R.id.button_mode_car
            QuestionDatabaseMode.RIDER -> R.id.button_mode_rider
        }
        questionModeToggleGroup.check(id)
    }

    override fun initUI() {
        val startTestButton = findViewById<Button>(R.id.button_start_test)
        startTestButton.setOnClickListener {
            presenter.startTestButtonPressed()
        }
        val browseQuestionsButton = findViewById<Button>(R.id.button_browse_questions)
        browseQuestionsButton.setOnClickListener {
            presenter.browseQuestionsButtonPressed()
        }

        val questionModeToggleGroup =
            findViewById<MaterialButtonToggleGroup>(R.id.toggleQuestionMode)
        questionModeToggleGroup.addOnButtonCheckedListener(MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->
            Log.d(packageName, "toggle mode: checkedId $checkedId carId=${R.id.button_mode_car} riderId=${R.id.button_mode_rider}")
            when(checkedId) {
                R.id.button_mode_car -> presenter.questionModeToggled("CAR")
                R.id.button_mode_rider -> presenter.questionModeToggled("RIDER")
            }
        })
    }
}