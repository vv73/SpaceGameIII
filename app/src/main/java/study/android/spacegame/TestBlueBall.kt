package study.android.spacegame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Renderable
import study.android.spacegame.framework.Updatable


class TestBlueBall(game: GameView) : Updatable, Renderable {
    // генерируем случайные координаты в пределах окна
    var x: Float = Math.random().toFloat() * game.width
    var y: Float = Math.random().toFloat() * game.height
    var paint: Paint = Paint()
    override fun render(canvas: Canvas) {
        canvas.drawCircle(x, y, 3f, paint)
    }

    override fun update(deltaTime: Float) {
        //каждый update немного смещаем объект
        x += (Math.random() * 6 - 3).toFloat()
        y += (Math.random() * 6 - 3).toFloat()
    }

    init {
        paint.setColor(Color.BLUE)
    }
}
