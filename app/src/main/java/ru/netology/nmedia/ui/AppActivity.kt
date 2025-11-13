package ru.netology.nmedia.ui

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val statsView = findViewById<StatsView>(R.id.stats)
        val textView = findViewById<TextView>(R.id.label)

        statsView.data = listOf(
            500F,  // Фиолетовый - 25% (первый - перекрывает)
            500F,  // Бирюзовый - 25%
            500F,  // Желтый - 25%
            500F,  // Розово-красный - 25% (последний)
        )

        //  Создаем ValueAnimator для плавной анимации прогресса
        val progressAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200 // Общая длительность анимации
            repeatCount = ValueAnimator.INFINITE // Бесконечное повторение
            repeatMode = ValueAnimator.RESTART // Начинать заново

            addUpdateListener { animator ->
                val progress = animator.animatedValue as Float
                statsView.progress = progress
                textView.text = "Progress: ${(progress * 100).toInt()}%"
            }
        }

        // Запускаем анимацию
        progressAnimator.start()

        // Обычную анимацию View можно отавить прикольно получается
        /*     val scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.animation)

               .apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        textView.text = "AnimationStart"
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        textView.text = "AnimationEnd"
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        textView.text = "AnimationRepeat"
                    }
                })
            }

        statsView.startAnimation(scaleAnimation)  */
    }
}