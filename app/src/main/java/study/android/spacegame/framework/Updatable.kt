package study.android.spacegame.framework


/**
  * Что-то, что можно обновить
  */
interface Updatable {
    /**
     * Обновляет объект
     */
    fun update(deltaTime: Float)
}