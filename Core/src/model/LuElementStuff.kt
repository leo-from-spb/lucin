@file:JvmName("LuElementStuff")

package lb.lucin.core.model


fun CharSequence.reduceWithEllipsis(limit: Int): CharSequence {
    val n = this.length
    if (n <= limit) {
        return this
    }
    else {
        val m1 = (limit - 1)/2
        val m2 = limit - 1 - m1
        val b = StringBuilder(limit)
        b.append(this, 0, m1)
        b.append('…')
        b.append(this, n-m2, n)
        return b
    }
}
