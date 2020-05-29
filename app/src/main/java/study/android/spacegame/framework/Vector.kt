package study.android.spacegame.framework

/**
 * Простой класс двумерных векторов. Реализованы только нужные
 * в игре операции над векторами.
 *
 */
class Vector {
    var x = 0f
    var y = 0f

    constructor(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    constructor()

    operator fun set(x: Float, y: Float): Vector {
        this.x = x
        this.y = y
        return this
    }

    fun set(to: Vector): Vector {
        x = to.x
        y = to.y
        return this
    }

    /**
     * Добавляет к компонентам вектора данные значения
     */
    fun add(x: Float, y: Float): Vector {
        this.x += x
        this.y += y
        return this
    }

    fun add(what: Vector): Vector {
        x += what.x
        y += what.y
        return this
    }

    fun sub(what: Vector): Vector {
        x -= what.x
        y -= what.y
        return this
    }

    fun sub(x: Float, y: Float): Vector {
        this.x -= x
        this.y -= y
        return this
    }

    /**
     * Умножает вектор на величину
     */
    fun scale(scl: Float): Vector {
        x *= scl
        y *= scl
        return this
    }

    /**
     * Умножает вектор на вектор
     */
    fun scale(x: Float, y: Float): Vector {
        this.x *= x
        this.y *= y
        return this
    }

    /**
     * Вычисляет квадрат расстояния между точками
     */
    fun dst2(to: Vector): Float {
        return (Math.pow(
            to.x - x.toDouble(),
            2.0
        ) + Math.pow(to.y - y.toDouble(), 2.0)).toFloat()
    }

    /**
     * Нормализует вектор
     */
    fun norm(): Vector {
        val d = Math.sqrt(dst2(Vector()).toDouble())
        x /= d.toFloat()
        y /= d.toFloat()
        return this
    }

    /**
     * Интерполирует вектор между ним и вторым вектором.
     */
    fun interpolate(to: Vector, progress: Float): Vector {
        x = x + (to.x - x) * progress
        y = y + (to.y - y) * progress
        return this
    }

    /**
     * Просто для красоты переопределим toString
     */
    override fun toString(): String {
        return "($x;$y)"
    }
}
