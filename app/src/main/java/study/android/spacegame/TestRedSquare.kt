package study.android.spacegame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Renderable
import study.android.spacegame.framework.Touchable
import study.android.spacegame.framework.Updatable


class TestRedSquare(var game: GameView) : Updatable, Renderable, Touchable {
    var x: Float = Math.random().toFloat() * game.width
    var y: Float = Math.random().toFloat() * game.height
    var size = 0f
    var paint: Paint = Paint()
    var rect: RectF = RectF(0f, 0f, 0f, 0f)
    override fun render(canvas: Canvas) {
        canvas.drawRect(rect, paint)
    }

    override fun update(deltaTime: Float) {
        // просто увеличиваем размер
        size += 2
        rect = RectF(x, y, x + size , y + size)
    }

    override fun onScreenTouch(touchX: Float, touchY: Float, justTouched: Boolean): Boolean {
        // если попали в прямоугольник - удаляем объект из игры
        if (rect.contains(touchX, touchY)) {
            game.removeObject(this)
            return true;
        } else {
            return false
        }
    }
    init{
        paint.setColor(Color.RED)
    }
}
