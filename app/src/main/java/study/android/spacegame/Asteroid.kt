package study.android.spacegame

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import study.android.spacegame.framework.*


/**
 * Это наши астероиды.
 */
class Asteroid (val image: Bitmap, val gameView: GameView, val activity:MainActivity): Touchable, Renderable, Updatable{

    // Текущая координата
   var coord: Vector

    // Угол поворота
    var spin = 0f

    // Скорость поворота
    var spin_speed = (Math.random() * 100 - 50).toFloat()

    // Размер астероида в пикселях
    var diameter = 1f

    // Скорость
    var velocity: Vector

    val center = Vector(gameView.width.toFloat() / 2, (gameView.height / 2).toFloat())

    init{
        // Генерируем случайный вектор (точку) начала движения
        coord = Vector(
            Math.random().toFloat(),
            Math.random().toFloat()
        ).scale(gameView.width.toFloat(), gameView.height.toFloat())

        // Генерируем вектор скорости (разлет от центра экрана)
        velocity = Vector(coord.x, coord.y).sub(center)
        // Нормализуем его
            .norm()
    }

    var dst = RectF()
    override fun onScreenTouch(touchX: Float, touchY: Float, justTouched: Boolean): Boolean {
        // просто удаляем его, если по нему нажали только что,
        // Для определения расположения используем dst - квадрат,
        // в который был нарисован астероид в последнем обновлении.
        if (justTouched && dst.contains(touchX, touchY)) {
            gameView.removeObject(this);
            //и добавляем взрыв
            gameView.addObject(
                Explosion(gameView.getBitmap(R.drawable.explosion1), this, gameView))
                //добавляем 10 очков
                activity.addScore(10)
            return true
        }
        return false;
    }

    val tmpVector: Vector = Vector(0f, 0f)

    override fun update(deltaTime: Float) {
        // Обновим значение поворота
        spin += deltaTime * spin_speed;
        // Изменяем положение астероида
        // чем ближе, тем быстрее
        velocity = velocity.scale((1 + 0.05 * deltaTime).toFloat());
        // Мы будем увеличивать диаметр астероида, как будто он приближается
        diameter *= 1 + 0.5f * deltaTime
        coord.add(tmpVector.set(velocity).scale((deltaTime * 20)));
        if (!dst.intersect(RectF(0f, 0f, gameView.width.toFloat(), gameView.height.toFloat()))) {
            gameView.removeObject(this)
        }
        if (diameter > gameView.getHeight() / 2) {
            gameView.removeObject(this);
            //и добавляем взрыв
            gameView.addObject(
                Explosion(gameView.getBitmap(R.drawable.explosion2), this, gameView)
            );
            activity.damage()
        }
    }
    override fun render(canvas: Canvas) {
        canvas.save(); //Saving the canvas and later restoring it so only this image will be rotated.
        canvas.rotate(-spin, coord.x, coord.y);
        dst.set(coord.x - diameter/2, coord.y - diameter/2, coord.x + diameter/2, coord.y + diameter/2)
        canvas.drawBitmap(image, null, dst, null)
        canvas.restore();
    }

    override fun zOrder(): Float {
        // Положение астероида у нас зависит от его размера
        return diameter
    }


}