package lb.lucin.core.parser

import lb.lucin.core.model.LuFile
import lb.lucin.core.model.LuMatter
import lb.lucin.core.model.LuMatterKind
import lb.lucin.core.model.LuMatterKind.*

/**
 * Lexer-tokenizer.
 * Reads the given file by tokens and returns them as matters.
 */
class LucinLexer (val file: LuFile) {

    val text = file.text
    val textLength = text.length

    val tabLength = 8


    /**
     * The offset of the tail (starts with 0) — the tail will be the begin of a new token.
     */
    private var tailOffset = 0

    /**
     * The line number of the tail (starts from 1).
     */
    private var tailLineNr = 1

    /**
     * The position in the line of the tail (starts from 1).
     */
    private var tailPos = 1


    /**
     * The current offset of the head (starts from 0) — the head will be right after the end of a new token.
     */
    private var headOffset = 0

    /**
     * The current line of the head (starts from 1).
     */
    private var headLineNr = 1

    /**
     * The current position in the line of the head (starts from 1).
     */
    private var headPos = 1

    /**
     * The current character (at the head).
     */
    private var currChar: Char = if (textLength > 0) text[0] else '\u0000'

    /**
     * The next character.
     */
    private var nextChar: Char = if (textLength > 1) text[1] else '\u0000'

    /**
     * The char that was before the current one.
     */
    private var prevChar: Char = '\u0000'



    /**
     * Takes the next token and shift positions.
     */
    fun takeNextMatter(): LuMatter {
        if (headOffset >= textLength) return LuMatter(file, null, headOffset, 0, headLineNr, headPos, mEOT)

        var m: LuMatter? = null
        when {
            currChar.isWhitespace() -> m = takeSpace()
            currChar.isWordChar -> m = takeWord()
            currChar in punctuationSymbols -> m = takePunctuationSymbol()
            else -> m = takeUnknownLexem()
        }

        m = m ?: takeUnknownLexem()
        return m
    }

    private fun takeSpace(): LuMatter? {
        while (currChar.isWhitespace() && prevChar != '\n') step()
        return makeLexemAndAdvance(mSpace)
    }

    private fun takeWord(): LuMatter? {
        var isBig = currChar.isUpperCase()
        while (currChar.isWordChar) {
            isBig = isBig && (currChar.isUpperCase() || currChar.isDigit())
            step()
        }
        return makeLexemAndAdvance(if (isBig) mBigWord else mWord)
    }

    private fun takePunctuationSymbol(): LuMatter? {
        while (currChar in punctuationSymbols) step()
        return makeLexemAndAdvance(mSymbol)
    }

    private fun takeUnknownLexem(): LuMatter {
        step()
        while (currChar == prevChar && currChar != '\u0000') step()
        val anUnknownLexem = makeLexemAndAdvance(mUnknown)
        assert(anUnknownLexem != null)
        return anUnknownLexem!!
    }

    private fun makeLexemAndAdvance(kind: LuMatterKind): LuMatter? {
        val length = headOffset - tailOffset
        if (length <= 0) return null
        val matter = LuMatter(file, null, tailOffset, length, tailLineNr, tailPos, kind)
        advance()
        return matter
    }

    private fun step() {
        headOffset++
        headPos++
        if (currChar == '\n') {
            headLineNr++
            headPos = 1
        }
        currChar = nextChar
        nextChar = if (headOffset + 1 < textLength) text[headOffset + 1] else '\u0000'
    }

    private fun advance() {
        tailOffset = headOffset
        tailLineNr = headLineNr
        tailPos = headPos
    }

}