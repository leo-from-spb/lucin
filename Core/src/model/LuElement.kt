package lb.lucin.core.model

import lb.lucin.core.model.LuMatterKind.mSpace

/**
 * Abstract element of the Lucin syntax tree.
 */
sealed class LuElement {

    abstract val parentElement: LuElement?

}


class LuFile : LuElement {

    override val parentElement: LuElement?
        get() = null

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

    override fun toString() = "$fileName($text)"

}


/**
 * Lucin element that contains a text or space, or anything located in a source file.
 *
 * @property file           the file this matter belongs to.
 * @property parentMatter   the parent matter; or null when the parent is the file.
 * @property offset         the offset in the file, starting with 0.
 * @property length         the length of the text.
 * @property lineNr         the line number started with 1.
 * @property pos            the position in the line started with 1.
 * @property siblingL       the sibling at the left.
 * @property siblingR       the sibling at the right.
 * @property childF         the first child.
 * @property childL         the last child.
 * @property childrenCount  count of the children.
 */
open class LuMatter : LuElement {

    @JvmField
    val file: LuFile

    var parentMatter: LuMatter?
        private set

    val offset: Int
    val length: Int
    val lineNr: Int
    val pos: Int

    var siblingL: LuMatter? = null
        private set
    var siblingR: LuMatter? = null
        private set
    var childF: LuMatter? = null
        private set
    var childL: LuMatter? = null
        private set
    var childrenCount: Int = 0
        private set

    final override val parentElement: LuElement
        get() = parentMatter ?: file

    var kind: LuMatterKind


    constructor(file: LuFile, parentMatter: LuMatter?, offest: Int, length: Int, lineNr: Int, pos: Int, kind: LuMatterKind = mSpace) {
        if (parentMatter != null) assert(file === parentMatter.file)

        this.file = file
        this.parentMatter = parentMatter
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

    fun children(): Sequence<LuMatter> =
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

    fun setParent(parentMatter: LuMatter) {
        assert(parentMatter.file === file)
        this.parentMatter = parentMatter
    }

    fun removeParent() {
        parentMatter = null
    }

    infix fun addChildAtTheEnd(child: LuMatter) {
        assert(child.siblingL == null || child.siblingR === null)
        child.setParent(this)
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
        child.setParent(this)
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


    override fun toString() = "${kind.code}[$lineNr:$pos/$offset:$length]${text.reduceWithEllipsis(60)}"

}
