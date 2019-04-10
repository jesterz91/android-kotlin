package io.github.jesterz91.databinding

import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter

/**
 *  @BindingAdapter 을 사용하여 사용자 지정 속성 정의
 *  (첫 번째 매개 변수는, 해당 속성을 사용하는 뷰)
 */
object BindingAdapter {

    @BindingAdapter("app:sentimentIcon")
    @JvmStatic
    fun sentimentIcon(view: ImageView, like: Int) {
        when {
            like > 9 -> {
                view.setImageResource(R.drawable.ic_sentiment_very_satisfied_black_24dp)
            }
            like > 4 -> {
                view.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp)
            }
            else -> {
                view.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp)

            }
        }
    }

    @BindingAdapter("app:progressScaled", "android:max", requireAll = true)
    @JvmStatic
    fun setProgress(progressBar: ProgressBar, likes: Int, max: Int) {
        progressBar.progress = (likes * max / 10).coerceAtMost(max)
    }
}