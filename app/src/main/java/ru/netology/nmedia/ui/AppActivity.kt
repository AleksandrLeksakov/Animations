package ru.netology.nmedia.ui

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
        val view = findViewById<StatsView>(R.id.stats)
        binding.stats.data = listOf(
            500F,  // Фиолетовый - 25% (первый - перекрывает)
            500F,  // Бирюзовый - 25%
            500F,  // Желтый - 25%
            500F,  // Розово-красный - 25% (последний)
        )

        val textView = findViewById<TextView>(R.id.label)

        view.startAnimation(
            AnimationUtils.loadAnimation(this, R.anim.animation).apply {
                setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationEnd(animation: Animation?) {
                        textView.text = "AnimationEnd"
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        textView.text = "AnimationRepeat"
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        textView.text = "AnimationStart"
                    }

                })
            }
        )
        // binding.stats.overlap = 4F // Оптимальное перекрытие
    }
}