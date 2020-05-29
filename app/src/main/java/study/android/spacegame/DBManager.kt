package study.android.spacegame

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteStatement


/**
 * Класс, через который мы общаемся с нашей базой
 */
class DBManager(ctx: Context) {
    private val db: SQLiteDatabase

    /**
     * Запрос на вставку нового элемента
     */
    private val insert: SQLiteStatement

    /**
     * Перечисляет все записи в базе данных, сортируя их по времени добавления
     * (сначала самые новые)
     */
    fun maxScore(): Int {
        val cursor: Cursor = db.rawQuery("select MAX(score) from Scores", null)
        cursor.moveToFirst()
        return cursor.getInt(0)
    }

    /**
     * Вставляет новый счет Этот метод нельзя вызывать из нескольких потоков
     * сразу.
     */
    fun insert(score: Int) {
        insert.bindLong(1, score.toLong())
        insert.execute()
    }

    companion object {
        var instance: DBManager? = null
        const val CURSOR_SCORE = "score"
        fun getInstance(ctx: Context): DBManager? {
            /*
		 * Если не существует экземпляра, то создаём новый, иначе возвращаем
		 * лежащий у нас. Не используйте такой примитивный способ создания
		 * синглтона, если планируете использовать его из нескольких потоков -
		 * это не безопасно.
		 */
            return if (instance != null) instance else DBManager(
                ctx
            ).also { instance = it }
        }
    }

    init {
        /*
		 * Открывает или создаёт нашу базу
		 */
        db = ctx.openOrCreateDatabase("scores.sqlite", Context.MODE_PRIVATE, null)
        db.execSQL(
            "create table if not exists Scores (" +  // Важно иметь _id, иначе не будут работать CursorAdapter-ы
                    "_id integer primary key," + "score integer" + ");"
        )
        /* Заодно с этим создадим наши шаблон запроса вставки */insert =
            db.compileStatement("insert into Scores " + "(score) values (?);")
    }
}
