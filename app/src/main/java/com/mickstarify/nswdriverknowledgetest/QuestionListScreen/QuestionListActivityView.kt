package com.mickstarify.nswdriverknowledgetest.QuestionListScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mickstarify.nswdriverknowledgetest.Database.Question
import com.mickstarify.nswdriverknowledgetest.R

class QuestionListActivityView : AppCompatActivity(), View {
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_list_screen_view)

        presenter = QuestionListActivityPresenter(this, this)
    }

    override fun initUI() {
        setTitle("Question List")
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun populateList(questionListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView_question_list)
        recyclerView.adapter = questionListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun pressedQuestion(questionTitle: String){

    }
}