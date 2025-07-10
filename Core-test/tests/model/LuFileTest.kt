package lb.lucin.core.model

import org.junit.jupiter.api.Test

import lb.yaka.base.expectations.*
import lb.yaka.base.gears.*

class LuFileTest {

    @Test
    fun microFile() {
        val text = "Simplicity is the ultimate sophistication."
        val file = LuFile(text, "test.txt")

        file.textLength aka "TextLength" equalsTo text.length
        file.text aka "Text" equalsTo text
    }

}