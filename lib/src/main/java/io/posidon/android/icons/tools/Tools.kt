package io.posidon.android.icons.tools

object Tools {
    fun searchOptimize(s: String) = s.lowercase()
        .replace('ñ', 'n')
        .replace('e', '3')
        .replace('a', '4')
        .replace('i', '1')
        .replace('¿', '?')
        .replace('¡', '!')
        .replace("wh", "w")
        .replace(Regex("(k|cc|ck)"), "c")
        .replace(Regex("(z|ts|sc|cs|tz)"), "s")
        .replace(Regex("([-'&/_,.:;*\"]|gh)"), "")
}