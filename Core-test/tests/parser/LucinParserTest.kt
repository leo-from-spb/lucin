package lb.lucin.core.parser

import lb.lucin.core.model.LuMatter
import lb.yaka.base.expectations.containsExactly
import lb.yaka.base.gears.expect
import org.junit.jupiter.api.Test


class LucinParserTest {

    @Test
    fun basicOneLine() {
        val text = "Knowledge is power."
        val parser = LucinParser()
        val file = parser.parseSourceFile(text, "Test file")

        val content: List<LuMatter> = file.children().filterIsInstance<LuMatter>().toList()
        val t = content.map { it.text.toString() }

        expect that t containsExactly arrayOf("Knowledge", " ", "is", " ", "power", ".")
    }

}