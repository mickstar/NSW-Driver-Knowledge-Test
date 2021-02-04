package com.mickstarify.dktcar.QuestionListScreen

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.mickstarify.dktcar.Database.QuestionDatabaseMode
import com.mickstarify.dktcar.Database.Room.QuestionStatus
import com.mickstarify.dktcar.Database.Room.QuestionStatusDatabase
import com.mickstarify.dktcar.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.schedulers.Schedulers
import java.lang.Exception

class QuestionListAdapter(context: Context, val questions: List<QuestionCategoryObject>, val mode: QuestionDatabaseMode, val listener: QuestionListAdapterListener?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class CategoryViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.textView_header_title)

        }
    }

    class QuestionViewHolder(view: android.view.View, val listener: QuestionListAdapterListener?) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val imageView: ImageView
        var questionId : Int = 0
        init {
            textView = view.findViewById(R.id.textView_entry_title)
            imageView = view.findViewById(R.id.imageView_question_status)
            view.setOnClickListener(View.OnClickListener {
                Log.d("dktcar", "pressed ${textView.text}")
                listener?.onQuestionPressed(questionId)
            })
        }

    }

    val questionStatusDatabase = QuestionStatusDatabase(context)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var inflater = LayoutInflater.from(viewGroup.context)
        return when (viewType) {
            CATEGORY -> {
                CategoryViewHolder(
                    inflater.inflate(
                        R.layout.question_list_header_row,
                        viewGroup,
                        false
                    )
                )
            }
            QUESTION -> {
                QuestionViewHolder(
                    inflater.inflate(
                        R.layout.question_list_entry_row,
                        viewGroup,
                        false
                    ),
                    listener
                )
            }
            else -> {
                throw Exception("error invalid type")
            }
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        when (viewHolder.getItemViewType()) {
            CATEGORY -> {
                (viewHolder as CategoryViewHolder).textView.text =
                    questions[position].getCategory().title
            }
            QUESTION -> {
                val question = questions[position].getQuestion()
                (viewHolder as QuestionViewHolder).textView.text =
                    "${question.title} - ${question.question}"
                (viewHolder as QuestionViewHolder).questionId = question.id
                (viewHolder as QuestionViewHolder).imageView.visibility = View.INVISIBLE
                questionStatusDatabase.getQuestionStatus(question.title, mode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(Consumer<Int> {
                        when (it) {
                            QuestionStatus.ANSWERED_CORRECT -> {
                                (viewHolder as QuestionViewHolder).imageView.visibility = View.VISIBLE
                                (viewHolder as QuestionViewHolder).imageView.setImageResource(R.drawable.graphics_tick)
                            }
                            QuestionStatus.ANSWERED_INCORRECT -> {
                                (viewHolder as QuestionViewHolder).imageView.visibility = View.VISIBLE
                                (viewHolder as QuestionViewHolder).imageView.setImageResource(R.drawable.graphics_cross)
                            }
                            QuestionStatus.NOT_ANSWERED -> {
                                (viewHolder as QuestionViewHolder).imageView.visibility = View.INVISIBLE
                            }
                        }
                    })
            }
        }
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    override fun getItemViewType(position: Int): Int {
        if (questions[position].isCategory()) {
            return CATEGORY
        }
        return QUESTION
    }

    companion object {
        private val CATEGORY = 1
        private val QUESTION = 2
    }

}