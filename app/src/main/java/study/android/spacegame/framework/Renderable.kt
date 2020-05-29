package study.android.spacegame.framework

import android.graphics.Canvas

/**
 * То, что мы можем нарисовать
 */
interface Renderable: ZOrdered {
    /**
     * Рисует объект на данной канве.
     */
    fun render(canvas: Canvas)
}
