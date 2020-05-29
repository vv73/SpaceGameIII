package study.android.spacegame

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Renderable
import study.android.spacegame.framework.Updatable
import study.android.spacegame.framework.Vector


/**
 * Это будет наш звездный фон
 */
 class Stars(gameView: GameView) : Renderable, Updatable {

    // Тут у нас будут храниться координаты и "яркость" звёзд
    var coords: ArrayList<Vector> = ArrayList<Vector>(STAR_NUM)
    var alpha = ArrayList<Int>(STAR_NUM)
    var paint: Paint = Paint()

    override fun render(canvas: Canvas) {
        // Рисуем черное небо
        canvas.drawColor(Color.BLACK)
        // Рисуем звёзды
        paint.setColor(Color.WHITE)
        for (i in 0 until coords.size) {
            val coord: Vector = coords[i]
            paint.setAlpha(alpha[i])
            canvas.drawPoint(coord.x, coord.y, paint)
        }
    }

    override fun update(deltaTime: Float) {
        // Применяем изменение alpha на все звёзды
        for (i in 0 until coords.size) {
            // Достаём данные
            var a = alpha[i]
            a += (Math.random() * 51 - 25).toInt()
            // Если выходим за рамки возможной прозрачности  0..255,
            // "возвращаем" новое значение в пределы.
            if (a > 255) a = 255
            if (a < 0) a = 0
            // Закладываем новые данные
            alpha[i] = a
        }
    }

    // Звезды рисуются подо всем.
    override fun zOrder(): Float {
        return -1.0f
    }

    companion object {
        // Количество звёзд
        const val STAR_NUM = 200
    }

    init {
        // Сейчас мы сгенерируем звезды
        for (i in 0 until STAR_NUM) {
            coords.add(
                Vector(
                    (Math.random() * gameView.width).toFloat(),
                    (Math.random() * gameView.height).toFloat()
                )
            )
            alpha.add((Math.random() * 256).toInt())
        }
    }
}