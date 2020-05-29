package study.android.spacegame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Updatable


class MainActivity : AppCompatActivity(), Updatable {
    lateinit var gameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cоздаем GameView
        gameView = GameView(this, null)
        // Устанавливаем наш GameView на активность
        setContentView(gameView)

        gameView.addObject(this)
        gameView.doOnLayout{gameView.addObject(Stars(gameView))}

    }

    // прошедшее время с последнего добавления
    var timeElapsed = 0f
    override fun update(deltaTime: Float) {
        // Каждую секунду добавляем объект
        if (timeElapsed > 1) {
            gameView.addObject(Asteroid(gameView.getBitmap(R.drawable.asteroid), gameView))
            // обнуляем время ожидания
            timeElapsed = 0f
        } else {
            // игровая секунда еще не прошла - ждем
            timeElapsed += deltaTime
        }
    }
}
