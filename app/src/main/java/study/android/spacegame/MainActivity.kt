package study.android.spacegame

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Updatable


class MainActivity : AppCompatActivity(), Updatable {
    lateinit var game: GameView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Cоздаем GameView
        game = GameView(this, null)
        // Устанавливаем наш GameView на активность
        setContentView(game)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        //добавляем себя в игру
        game.addObject(this)
        for (i in 0..49) {
            game.addObject(TestBlueBall(game))
        }
    }

    // прошедшее время с последнего добавления
    var timeElapsed = 0f
    override fun update(deltaTime: Float) {
        // Каждую секунду добавляем объект
        if (timeElapsed > 1) {
            game.addObject(TestRedSquare(game))
            // обнуляем время ожидания
            timeElapsed = 0f
        } else {
            // игровая секунда еще не прошла - ждем
            timeElapsed += deltaTime
        }
    }
}
