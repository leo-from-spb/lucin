package lb.lucin.core

import lb.lucin.core.model.LuFile
import lb.lucin.core.model.LuMatterTest
import lb.lucin.core.parser.LucinLexerTest
import lb.lucin.core.parser.LucinParserTest
import org.junit.jupiter.api.TestInstance
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName

@Suite
@SuiteDisplayName("CoreTestSuite")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SelectClasses(
    LuMatterTest::class,
    LuFile::class,
    LucinLexerTest::class,
    LucinParserTest::class,
)
class CoreTestSuite
