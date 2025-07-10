package lb.lucin.core.parser

import lb.lucin.core.model.LuFile
import lb.lucin.core.model.LuMatter
import lb.lucin.core.model.LuMatterKind

/**
 * Lucino-text parser.
 */
class LucinParser {

    fun parseSourceFile(text: String, name: String): LuFile {
        val file = LuFile(text, name)
        val lexer = LucinLexer(file)

        var m: LuMatter = lexer.takeNextMatter()
        while (m.kind != LuMatterKind.mEOT) { // kotlin has no the repeat...until loop

            m = lexer.takeNextMatter()
        }



        return file
    }

}