package study.android.spacegame.framework

//Cравниватель ZOrder-а. Если оба объекта имеют z-order, то сравниваем значения,
// иначе приоритетным считается объект с имеющимся zorder-ом.
class ZComparator : Comparator<Any?> {
    override fun compare(lhs: Any?, rhs: Any?): Int {
        if (lhs is ZOrdered && rhs is ZOrdered) return if (lhs.zOrder() - rhs.zOrder() > 0) 1 else -1
        if (lhs is ZOrdered) return 1
        return if (rhs is ZOrdered) -1 else 0
    }
}
