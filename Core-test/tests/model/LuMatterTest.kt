package lb.lucin.core.model

import lb.yaka.base.expectations.containsExactly
import lb.yaka.base.expectations.equalsTo
import lb.yaka.base.expectations.hasSize
import lb.yaka.base.gears.expect
import org.junit.jupiter.api.Test

class LuMatterTest {

    @Test
    fun addChildrenNormal() {
        val f = LuFile("Possible Text", "Test file")
        val p = LuMatter(f, null, 0, 4, 1, 1)
        val w1 = LuMatter(f, p, 0, 1, 1, 1)
        val w2 = LuMatter(f, p, 1, 2, 1, 2)
        val w3 = LuMatter(f, p, 2, 3, 1, 3)
        val w4 = LuMatter(f, p, 3, 4, 1, 4)

        p addChildAtTheEnd w1
        p addChildAtTheEnd w2
        p addChildAtTheEnd w3
        p addChildAtTheEnd w4

        expect that p.childrenCount equalsTo 4

        val r = p.children().toList()
        expect that r hasSize 4 containsExactly arrayOf(w1,w2,w3,w4)
    }

    @Test
    fun addChildrenReverse() {
        val f = LuFile("Possible Text", "Test file")
        val p = LuMatter(f, null, 0, 4, 1, 1)
        val w1 = LuMatter(f, p, 0, 1, 1, 1)
        val w2 = LuMatter(f, p, 1, 2, 1, 2)
        val w3 = LuMatter(f, p, 2, 3, 1, 3)
        val w4 = LuMatter(f, p, 3, 4, 1, 4)

        p addChildAtBegin w4
        p addChildAtBegin w3
        p addChildAtBegin w2
        p addChildAtBegin w1

        expect that p.childrenCount equalsTo 4

        val r = p.children().toList()
        expect that r hasSize 4 containsExactly arrayOf(w1,w2,w3,w4)
    }

}