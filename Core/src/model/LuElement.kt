package lb.lucin.core.model

import lb.lucin.core.model.LuMatterKind.mSpace

/**
 * Abstract element of the Lucin syntax tree.
 *
 * @property parent         the parent element, or null if it is a root.
 * @property siblingL       the sibling at the left.
 * @property siblingR       the sibling at the right.
 * @property childF         the first child.
 * @property childL         the last child.
 * @property childrenCount  count of the children.
 */
sealed class LuElement {

    var parent: LuElement? = null
        internal set(newParent) {
            field = newParent
        }

    var siblingL: LuElement? = null
        internal set
    var siblingR: LuElement? = null
        internal set
    var childF: LuElement? = null
        internal set
    var childL: LuElement? = null
        internal set
    var childrenCount: Int = 0
        internal set

    fun children(): Sequence<LuElement> =
        when (childrenCount) {
            0 -> emptySequence()
            1 -> sequenceOf(childF!!)
            else -> sequence {
                var child = childF
                while (child != null) {
                    yield(child)
                    child = child.siblingR
                }
            }
        }

    infix fun addChildAtTheEnd(child: LuElement) {
        assert(child.siblingL == null || child.siblingR === null)
        child.parent = this
        if (childL == null) {
            assert(childF == null)
            childF = child
            childL = child
            childrenCount = 1
        }
        else {
            child.siblingL = childL
            childL!!.siblingR = child
            childL = child
            childrenCount++
        }
    }

    infix fun addChildAtBegin(child: LuMatter) {
        assert(child.siblingL === null || child.siblingR == null)
        child.parent = this
        if (childF == null) {
            assert(childL == null)
            childL = child
            childF = child
            childrenCount = 1
        }
        else {
            child.siblingR = childF
            childF!!.siblingL = child
            childF = child
            childrenCount++
        }
    }

}


class LuFile : LuElement {

    @JvmField
    val text: String

    @JvmField
    val textLength: Int

    @JvmField
    val fileName: String

    constructor(text: String, fileName: String) {
        this.text = text
        this.textLength = text.length
        this.fileName = fileName
    }

    internal fun assignRawContent(matters: Sequence<LuMatter>) {
        assert(childrenCount == 0)
        assert(childF == null && childL == null)

        val mF: LuMatter
        var mL: LuMatter
        var count: Int

        val iterator = matters.iterator()
        if (iterator.hasNext()) {
            val m = iterator.next()
            m.parent = this
            mF = m
            mL = m
            count = 1
        }
        else {
            return
        }

        while (iterator.hasNext()) {
            val m = iterator.next()
            m.parent = this
            mL.siblingR = m
            m.siblingL = mL
            mL = m
            count++
        }

        this.childF = mF
        this.childL = mL
        this.childrenCount = count
    }


    override fun toString() = "$fileName(${text.reduceWithEllipsis(60)})"

}


/**
 * Lucin element that contains a text or space, or anything located in a source file.
 *
 * @property file           the file this matter belongs to.
 * @property offset         the offset in the file, starting with 0.
 * @property length         the length of the text.
 * @property lineNr         the line number started with 1.
 * @property pos            the position in the line started with 1.
 */
open class LuMatter : LuElement {

    @JvmField
    val file: LuFile

    val offset: Int
    val length: Int
    val lineNr: Int
    val pos: Int

    var kind: LuMatterKind


    constructor(file: LuFile, parentMatter: LuMatter?, offest: Int, length: Int, lineNr: Int, pos: Int, kind: LuMatterKind = mSpace) {
        if (parentMatter != null) assert(file === parentMatter.file)

        this.file = file
        this.offset = offest
        this.length = length
        this.lineNr = lineNr
        this.pos = pos
        this.kind = kind
    }

    constructor(parentMatter: LuMatter, offest: Int, length: Int, lineNr: Int, pos: Int, kind: LuMatterKind = mSpace)
        : this(parentMatter.file, parentMatter, offest, length, lineNr, pos, kind)


    val text: CharSequence
        get() = file.text.subSequence(offset, offset + length)



    override fun toString() = "${kind.code}[$lineNr:$pos/$offset:$length]${text.reduceWithEllipsis(60)}"

}
