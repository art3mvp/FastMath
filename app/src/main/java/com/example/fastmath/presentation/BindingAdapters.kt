package com.example.fastmath.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.fastmath.R

interface OnOptionClickListener {
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswer")
fun bindRequiredAnswer(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, percentage: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_correct_percentage),
        percentage
    )
}

@BindingAdapter("userScore")
fun bindUserScore(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_score),
        count
    )
}

@BindingAdapter("countOfRightAnswer", "countOfQuestions")
fun bindUserPercentage(textView: TextView, countOfRightAnswers: Int, countOfQuestions: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.user_percentage),
        getPercentage(countOfRightAnswers, countOfQuestions)
    )
}

private fun getPercentage(correctAns: Int, totalAns: Int): Int {
    return (correctAns / totalAns.toDouble() * 100).toInt()
}

@BindingAdapter("statusImage")
fun bindStatusImage(imageView: ImageView, status: Boolean) {
    val drawableResId = if (status) {
        R.drawable.happy_brain
    } else {
        R.drawable.sad_brain
    }
    imageView.setImageResource(drawableResId)
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enough))

}
@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enough: Boolean) {
    val color = getColorByState(progressBar.context, enough)
    progressBar.progressTintList = ColorStateList.valueOf(color)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        ContextCompat.getColor(context, R.color.green_correct)
    } else {
        ContextCompat.getColor(context, R.color.red_progress)
    }
    return colorResId
}
