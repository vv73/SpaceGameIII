package study.android.spacegame

import android.graphics.*
import study.android.spacegame.framework.GameView
import study.android.spacegame.framework.Renderable
import study.android.spacegame.framework.Updatable
import study.android.spacegame.framework.Vector


/**
 * Всякие взрывы!
 */
class Explosion(val particle: Bitmap, from: Asteroid, val gameView: GameView )
                                               : Updatable, Renderable {

    companion object {
        // Сколько у нас частиц во взрыве
        const val PARTICLE_NUM = 10
    }


    // Текущий диаметр частицы
    private var diameter: Float

    // Начальный диаметр частицы
    private val start_diameter: Float

    // Скорости отдельных частиц.
    private val speeds: ArrayList<Vector> = ArrayList<Vector>(PARTICLE_NUM)

    // Центр взрыва
    private val center: Vector

    // zOrder взрыва
    private val z: Float
    var tmp: Vector = Vector()
    private val dst: Rect = Rect()
    var paint: Paint = Paint()
    override fun render(canvas: Canvas) {
        // Расстояние от эпицентра взрыва. Да, мы не используеми отдельных
        // переменных для интерполяции, просто считаем по разнице начального
        // диаметра и текущего. Так не приходится сохранять положение каждой
        // частицы и контролировать размер взрыва легче.
        val distance = (start_diameter - diameter) * 1.5f
        // Тут всё довольно просто, и рисуется всё так же, как и в астероиде.
        for (i in 0 until PARTICLE_NUM) {
            val speed: Vector = speeds[i]
            // Единственное отличие - поворот вычисляется на основе вектора,
            // это простейшая тригонометрия.
            val rotation =
                Math.toDegrees(Math.atan2(speed.y.toDouble(), speed.x.toDouble())).toFloat()
            tmp.set(speed).scale(distance).add(center)
            dst.set(0, 0, diameter.toInt(), diameter.toInt())
            dst.offset(tmp.x.toInt(), tmp.y.toInt())
            dst.offset((-diameter).toInt() / 2, (-diameter).toInt() / 2)
            paint.setColor(Color.WHITE)
            paint.setAlpha((255 * (diameter / start_diameter)).toInt())
            canvas.save()
            canvas.rotate(rotation, tmp.x, tmp.y)
            canvas.drawBitmap(particle, null, dst, paint)
            canvas.restore()
        }
    }

    override fun update(deltaTime: Float) {
        // Просто уменьшаем диаметр, а после того,
        // как он становится меньше 1 - совершаем суицид.
        diameter *= 1 - deltaTime
        if (diameter <= 1) gameView.removeObject(this)
    }

    // удаленность взрыва совпадает с удаленностью уничтоженного астероида
    override fun zOrder(): Float {
        return z
    }

    init {

        // Копируем данные о положении взрыва из астероида
        center = Vector().set(from.coord)
        diameter = from.diameter
        start_diameter = diameter
        z = from.zOrder()

        // Генерируем взрыв
        for (i in 0 until PARTICLE_NUM) {
            val vector: Vector = Vector(
                Math.random().toFloat(),
                Math.random().toFloat()
            ).sub(0.5f, 0.5f).norm()
                .scale(Math.random().toFloat())
            speeds.add(vector)
        }
    }
}
