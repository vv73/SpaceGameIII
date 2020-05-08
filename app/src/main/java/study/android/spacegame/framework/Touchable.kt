package study.android.spacegame.framework

interface Touchable : Updatable {
    /** Вызывается при нажатии на экран */
    fun onScreenTouch( touchX: Float, touchY: Float, justTouched: Boolean): Boolean
}
