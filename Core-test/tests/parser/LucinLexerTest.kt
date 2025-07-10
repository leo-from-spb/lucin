package lb.lucin.core.parser

import lb.lucin.core.model.LuFile
import lb.lucin.core.model.LuMatter
import lb.lucin.core.model.LuMatterKind.mEOT
import lb.yaka.base.expectations.containsExactly
import lb.yaka.base.gears.expect
import org.junit.jupiter.api.Test


class LucinLexerTest {

    @Test
    fun basicPhrase() {
        val file = LuFile("A word after a word after a word is power.", "Margaret Atwood")
        val lexer = LucinLexer(file)
        val matters = lexer.drainMatters()
        val lemmas = matters.map { it.text.toString() }

        val expected = arrayOf("A", " ", "word", " ", "after", " ", "a", " ", "word", " ", "after", " ", "a", " ", "word", " ", "is", " ", "power", ".")

        expect that lemmas containsExactly expected
    }

    private fun LucinLexer.drainMatters(): List<LuMatter> {
        val matters = ArrayList<LuMatter>()
        var m: LuMatter = this.takeNextMatter()
        while (m.kind != mEOT) {
            matters.add(m)
            m = this.takeNextMatter()
        }
        return matters
    }

}