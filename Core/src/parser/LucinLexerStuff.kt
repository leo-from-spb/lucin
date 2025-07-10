package lb.lucin.core.parser


val wordSymbols: Set<Char> = setOf('_', '#', '$', '@', '%', '\'')

val punctuationSymbols: Set<Char> = setOf(',', '.', ';', ':', '!', '?')




internal val Char.isWordChar: Boolean
    get() = this.isLetterOrDigit() || this in wordSymbols
