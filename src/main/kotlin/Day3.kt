/*
 * Copyright 2023 Mikhail Titov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

private const val INPUT =
    """
    """

private data class Num(
    val line: String,
    val lineIndex: Int,
    val at: Int,
    val data: Int
)

private data class Boundary(
    val line: String,
    val lineIndex: Int,
    val start: Int,
    val end: Int,
    val data: String
)

private data class Gear(
    val at: Pair<Int, Int>,
    val first: Num,
    var second: Num? = null
)

private val lines =
    INPUT.lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }

private val gears = mutableListOf<Gear>()

fun day3() {
    println("Running AoC Day 3 (1st part)...")
    firstPart()

    println()

    println("Running AoC Day 3 (2nd part)...")
    secondPart()
}

private fun firstPart() {
    val res = lines.numbers().sumOf {
        if (it.isAdjacentToSymbol()) it.data else 0
    }

    println("res: $res")
}

private fun secondPart() {
    lines.numbers().forEach {
        it.gears()
    }

    val res = gears
        .filter {
            it.second != null
        }
        .sumOf {
            it.first.data * requireNotNull(it.second?.data)
        }

    println("res: $res")
}

private fun List<String>.numbers() =
    flatMapIndexed { lineIndex, line ->
        line.numbers(lineIndex)
    }

private fun String.numbers(lineIndex: Int): List<Num> =
    buildList {
        var numString: String? = null
        var at: Int? = null


        this@numbers.forEachIndexed { index, char ->
            val isDigit = char.isDigit()

            if (isDigit) {
                numString = (numString ?: "") + char
                at = at ?: index
            }

            if (!isDigit || index == (length - 1)) {
                numString?.let { numStringNotNull ->
                    at?.let { atNotNull ->
                        val number = numStringNotNull.toInt()
                        val num = Num(
                            line = this@numbers,
                            lineIndex = lineIndex,
                            at = atNotNull,
                            data = number
                        )

                        add(num)

                        numString = null
                        at = null
                    }
                }
            }
        }
    }

private fun Num.isAdjacentToSymbol() =
    getBoundaries().any { boundary ->
        boundary.data.containsSymbol()
    }

private fun Num.gears() {
    getBoundaries(excludeInt = false).forEach { boundary ->
        boundary.data.forEachIndexed { index, char ->
            if (char == '*') {
                val coords = boundary.lineIndex to (boundary.start + index)
                getOrPutGear(coords, this@gears)
            }
        }
    }
}

private fun Num.getBoundaries(excludeInt: Boolean = true): List<Boundary> {
    val length = data.toString().length

    val range = getBoundaryRange(length)

    fun String.substring(excludeInt: Boolean = false) =
        substring(range).let { substring ->
            if (excludeInt) substring.filterNot { it.isDigit() } else substring
        }

    val topLineIndex = lineIndex - 1
    val topLineSub = lines.getOrNull(topLineIndex)?.substring()

    val currentLineSub = line.substring(excludeInt = excludeInt)

    val bottomLineIndex = lineIndex + 1
    val bottomLineSub = lines.getOrNull(bottomLineIndex)?.substring()

    return listOfNotNull(
        topLineSub?.to(topLineIndex),
        currentLineSub to lineIndex,
        bottomLineSub?.to(bottomLineIndex)
    ).map { (data, dataLineIndex) ->
        Boundary(
            line = line,
            lineIndex = dataLineIndex,
            start = range.first,
            end = range.last,
            data = data
        )
    }
}

private fun Num.getBoundaryRange(length: Int): IntRange {
    val start = if (at < 1) 0 else at - 1

    val endCandidate = at + length
    val end = if (line.length <= endCandidate) endCandidate - 1 else endCandidate

    return start..end
}

private fun String.containsSymbol() =
    this.contains("[^\\d.]".toRegex())

private fun getOrPutGear(at: Pair<Int, Int>, num: Num): Gear {
    val foundGear = gears.find {
        it.at == at
    }

    foundGear?.second = num

    return foundGear ?: Gear(at, first = num).also {
        gears.add(it)
    }
}