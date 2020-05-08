package study.android.spacegame.framework

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class GameView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var lastUpdate: Long = -1
    var timeScale = 1f

    var objects: ArrayList<Any> = ArrayList()
    var objectAddBuffer = ArrayList<Any>(10)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        lastUpdate = System.currentTimeMillis()
    }

    /**
     * Добавляет объект в список для обновления
     */
    fun addObject(o: Any) {
        objectAddBuffer.add(o)
    }

    /**
     * Удаляет объект из списка
     */
    fun removeObject(o: Any) {
        objects.remove(o)
    }

    /**
     * Удаляет все игровые объекты
     */
    fun clear() {
        objects.clear()
    }


    // только для тестирования
    var tmpX = 0f
    var tmpY = 100f
    var tmpPaint: Paint = Paint()

    // функция-заглушка
    fun touch(event: MotionEvent) {
        tmpX = event.x
        tmpY = event.y
    }

    /**
     * Рисует все наши объекты на хотсте.
     */
    fun render(canvas: Canvas) {
        // canvas.drawCircle(tmpX, tmpY, 20, tmpPaint);
        for (i in objects.indices) {
            val o = objects[i]
            /* *
             * Проверяем, может ли объект быть нарисован.
             * И рисуем, если может.
		    */
            if (o is Renderable) o.render(canvas)
        }
    }


    /**
     * Выполняет цикл обновления. deltaTime - это количество времени, прошедшее
     * с предыдущего обновления в секундах.
     */
    fun update(deltaTime: Float) {
        // tmpX += deltaTime * 10;
        var i = objects.size - 1
        while (i >= 0 && i < objects.size) {
            val o = objects[i]
            if (o is Updatable) o.update(deltaTime)
            i--
        }

        // Проверяем наш буфер добавленных объектов
        if (!objectAddBuffer.isEmpty()) {
            objects.addAll(objectAddBuffer)
            objectAddBuffer.clear()
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Для удобства переводим миллисекунды в секунды.
        // Дополнительно умножаем на скорость времени
        val currentTime = System.currentTimeMillis()
        // обновляем все объекты
        update((currentTime - lastUpdate) * 0.001f * timeScale)
        // Потом рисуем
        // Собственно рисование стоит вынести в отдельную функцию
        render(canvas)
        // А потом говорим системе, что мы хотим перерисоваться и в следующий раз.
        invalidate()
        // И выставляем новое время предыдушего запуска
        lastUpdate = currentTime
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // touch(event);
        val firstTouch = event.action == MotionEvent.ACTION_DOWN
        val touchX = event.x
        val touchY = event.y
        // в обратном порядке - ближние объекты получают событие первыми
        var i = objects.size - 1
        while (i >= 0 && i < objects.size) {
            val o = objects[i]
            if (o is Touchable) {
                o.onScreenTouch(touchX, touchY, firstTouch)
            }
            i--
        }
        return false
    }


}