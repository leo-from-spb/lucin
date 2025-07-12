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
        collectAndApplyRawContent(file)
        return file
    }

    private fun collectAndApplyRawContent(file: LuFile) {
        val lexer = LucinLexer(file)
        val rawContent: Sequence<LuMatter> =
            collectRawContent(lexer)
        file.assignRawContent(rawContent)
    }


    private fun collectRawContent(lexer: LucinLexer): Sequence<LuMatter> =
        sequence {
            var m: LuMatter = lexer.takeNextMatter()
            while (m.kind != LuMatterKind.mEOT) {
                yield(m)
                m = lexer.takeNextMatter()
            }
        }

}