package com.mickstarify.dktcar.QuestionScreen

import android.animation.Animator
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewAnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.mickstarify.dktcar.EntryScreen.EntryActivityView
import com.mickstarify.dktcar.R
import kotlin.math.max

class QuestionActivityView : AppCompatActivity(), View {
    lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_view)

        var mode = intent.getStringExtra(PARAM_MODE)
        if (mode != MODE_FORMAL_TEST && mode != MODE_SEQUENTIAL_QUESTIONS){
            throw Exception("Error invalid mode provided for questionActivity. Must be either formal test or sequential.")
        }

        presenter = QuestionActivityPresenter(this, mode)
    }

    override fun initUI() {
        val optionAButton = findViewById<Button>(R.id.button_option_A)
        val optionBButton = findViewById<Button>(R.id.button_option_B)
        val optionCButton = findViewById<Button>(R.id.button_option_C)

        optionAButton.setOnClickListener { presenter.submitAnswer(0) }
        optionBButton.setOnClickListener { presenter.submitAnswer(1) }
        optionCButton.setOnClickListener { presenter.submitAnswer(2) }
    }

    override fun displayQuestion(
        questionProgress: String,
        questionTitle: String,
        questionText: String,
        category: String,
        image: String,
        optionA: String,
        optionB: String,
        optionC: String
    ) {
        val questionProgressTextView = findViewById<TextView>(R.id.textView_question_progress)
        val questionIdTextView = findViewById<TextView>(R.id.textView_question_id)
        val questionTextTextView = findViewById<TextView>(R.id.textView_question_text)
        val optionAButton = findViewById<Button>(R.id.button_option_A)
        val optionBButton = findViewById<Button>(R.id.button_option_B)
        val optionCButton = findViewById<Button>(R.id.button_option_C)

        questionProgressTextView.text = questionProgress
        questionIdTextView.text = questionTitle
        questionTextTextView.text = questionText
        this.title = category
        optionAButton.text = optionA
        optionBButton.text = optionB
        optionCButton.text = optionC

        val imageView = findViewById<ImageView>(R.id.img_question_picture)
        if (image != "None"){
            imageView.visibility = android.view.View.VISIBLE
            val resName = image.replace(".png", "")
            imageView.setImageResource(resources.getIdentifier(resName, "drawable", this.packageName))
        } else {
            imageView.visibility = android.view.View.INVISIBLE
        }
    }



    override fun animateAnswer(
        correct: Boolean,
        buttonIndex: Int,
        onAnimationEnd: () -> Unit
    ) {
        val button = when(buttonIndex){
            0 -> findViewById<Button>(R.id.button_option_A)
            1 -> findViewById<Button>(R.id.button_option_B)
            2 -> findViewById<Button>(R.id.button_option_C)
            else -> throw(Exception("invalid button index"))
        }

//        val animator = AnimatorInflater.loadAnimator(this, R.animator.correct_animator)
//        animator.apply {
//            setTarget(button)
//            start()
//        }
        val reveal = findViewById<RelativeLayout>(R.id.fab_container)

        val x = button.x + button.width / 2
        val y = button.y + button.height / 2

        val listener = object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                reveal.visibility = android.view.View.INVISIBLE
                onAnimationEnd()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {
            }
        }

        if (correct) {
            reveal.setBackgroundColor(Color.GREEN)
        } else {
            reveal.setBackgroundColor(Color.RED)
        }
        reveal.visibility = android.view.View.VISIBLE

        val maxRadius = max(reveal.height, reveal.width)

        val myAnimator = ViewAnimationUtils.createCircularReveal(reveal, x.toInt(),
            y.toInt(),0f, maxRadius.toFloat())
        myAnimator.addListener(listener)
        myAnimator.start()

    }

    override fun showSuccessfullyCompletedTest() {
        Log.d("dktcar", "showing successfully completed dialog")
        val alert = AlertDialog.Builder(this)
            .setTitle("Passed Test!")
            .setMessage("You have successfully completed the drivers knowledge test.")
            .setPositiveButton("Ok", DialogInterface.OnClickListener {_,_ ->
                val intent = Intent(this, EntryActivityView::class.java)
                startActivity(intent)
            })
            .create()

        alert.show()
    }

    override fun showFailedTest() {
        Log.d("dktcar", "showing failed dialog")
        val alert = AlertDialog.Builder(this)
            .setTitle("Failed Test!")
            .setMessage("You have unfortunately failed the drivers knowledge test.")
            .setPositiveButton("Ok", DialogInterface.OnClickListener {_,_ ->
                val intent = Intent(this, EntryActivityView::class.java)
                startActivity(intent)
            })
            .create()

        alert.show()
    }

    override fun onBackPressed() {
        val alert = AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage("Are you sure you want to exit the test?")
            .setPositiveButton("Yes", DialogInterface.OnClickListener {_,_ ->
                super.onBackPressed()
            })
            .setNegativeButton("No", null)
            .create()

        alert.show()

    }

    companion object{
        val PARAM_MODE = "PARAM_MODE"
        val MODE_FORMAL_TEST = "FORMAL_TEST"
        val MODE_SEQUENTIAL_QUESTIONS = "SEQUENTIAL_TEST"
        val PARAM_QUESTION_ID = "PARAM_ID"
        val QUESTION_ID = "ID"
    }
}