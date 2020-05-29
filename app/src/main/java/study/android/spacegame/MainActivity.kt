package study.android.spacegame

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import kotlinx.android.synthetic.main.activity_main.*
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Updatable


class MainActivity : AppCompatActivity(), Updatable {
    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView = findViewById(R.id.gameView)
        gameView.addObject(this)
        gameView.doOnLayout{gameView.addObject(Stars(gameView))}
        pauseButton.setOnClickListener{pause(gameView)}
    }

    // прошедшее время с последнего добавления
    var timeElapsed = 0f
    override fun update(deltaTime: Float) {
        // Каждую секунду добавляем объект
        if (timeElapsed > 1) {
            gameView.addObject(Asteroid(gameView.getBitmap(R.drawable.asteroid), gameView, this))
            // обнуляем время ожидания
            timeElapsed = 0f
        } else {
            // игровая секунда еще не прошла - ждем
            timeElapsed += deltaTime
        }
    }

    fun pause(v: View?) {
        if (gameView.timeScale == 0f) {
            unpause()
            return
        }
        // Останавливаем время
        gameView.timeScale = 0f
        // И ещё добавляем перехват всех нажатий.
        gameView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                // Как только произошло нажатие, выключаем паузу
                unpause()
                return true
            }
        })
    }

    fun unpause() {
        // Возвращаем время и убираем перехват нажатий.
        gameView.timeScale = 1f
        gameView.setOnTouchListener(null)
    }

    var scoreValue = 0
    fun addScore(c: Int){
        scoreValue += c
        score.text = scoreValue.toString()
    }

    fun restart() {
        // Обнуляем очки
        scoreValue = 0
        score.text = "0"
        // Очищаем экран и добавляем объекты для игры
        gameView.clear()
        gameView.addObject(Stars(gameView))
        gameView.addObject(this)
        // Выставляем три жизни
        while (lives.childCount < 3) {
            val life = ImageView(this)
            life.setImageResource(R.drawable.ship)
            lives.addView(life)
        }
        // Снимаем игру с паузы
        unpause()
    }



    fun damage() {
        if (lives.childCount > 0) {
            lives.removeViewAt(0)
            return
        }
        pause(null)

        // Вставляем счет в таблицу рекордов
        DBManager.getInstance(this)!!.insert(scoreValue)

        // Показываем окошко GameOver-ом
        AlertDialog.Builder(this)
            .setMessage("Game over! Ваш счет: $scoreValue")
            .setCancelable(false)
            .setPositiveButton("Начать заново") {dialog, which-> restart() }
            .show()
    }

    fun records(v: View?) {
        pause(null)
        // Показываем окошко c рекордом
        AlertDialog.Builder(this)
            .setMessage("Абсолютный рекорд игры: " + DBManager.getInstance(this)!!.maxScore())
            .setCancelable(false)
            .setPositiveButton("Вернуться к игре") {dialog, which -> unpause()}
            .show()
    }
}
