package lb.lucin.core.model

/**
 * Kind of the matter.
 */
enum class LuMatterKind (val code: Char) {
    mSpace   ('-'),
    mWord    ('W'),
    mBigWord ('B'),
    mSymbol  ('S'),
    mLuSymbol('L'),
    mUnknown ('U'),
    mEOT     ('?')
}
