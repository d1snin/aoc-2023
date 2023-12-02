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

private val lines
    get() = INPUT.lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }

private val digitMap = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

fun day1() {
    println("Running AoC Day 1 (1st part)...")
    firstPart()

    println()

    println("Running AoC Day 1 (2nd part)...")
    secondPart()
}

private fun firstPart() {
    val res = lines.sumOfDigits()

    println("res: $res")
}

private fun secondPart() {
    val res = lines.transformWords().sumOfDigits()

    println("res: $res")
}

private fun List<String>.sumOfDigits() =
    sumOf { line ->
        val digits = line.filter {
            it.isDigit()
        }

        digits.singleOrNull()?.let {
            numberOf(it, it)
        } ?: numberOf(digits.first(), digits.last())
    }

private fun List<String>.transformWords() =
    map { line ->
        var transformedLine = line

        var limit = 0
        var reverse = false

        while (limit <= line.length) {
            val batch = if (reverse) transformedLine.takeLast(limit) else transformedLine.take(limit)
            var transformedBatch = batch

            digitMap.forEach { (word, digit) ->
                transformedBatch = transformedBatch.replace(word, digit)
            }

            val newTransformedLine = transformedLine.replace(batch, transformedBatch)
            val transformed = transformedLine != newTransformedLine

            if ((transformed || batch.contains("\\d".toRegex())) && !reverse) {
                reverse = true
                limit = 0
            } else {
                limit += 1
            }

            transformedLine = newTransformedLine
        }

        transformedLine
    }

private fun numberOf(first: Char, second: Char) =
    String(charArrayOf(first, second)).toInt()