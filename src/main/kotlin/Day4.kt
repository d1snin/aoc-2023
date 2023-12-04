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

private data class Card(
    val id: Int,
    val winning: List<Int>,
    val given: List<Int>
)

private val lines =
    INPUT.lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }

fun day4() {
    println("Running AoC Day 4 (1st part)...")
    firstPart()

    println()

    println("Running AoC Day 4 (2nd part)...")
    secondPart()
}

private fun firstPart() {
    val res = lines.parseCards()
        .sumOf {
            it.points()
        }

    println("res: $res")
}

private fun secondPart() {
    val res = lines
        .parseCards()
        .populate()
        .count()

    println("res: $res")
}

private fun List<String>.parseCards() =
    map {
        it.parseCard()
    }

private fun String.parseCard(): Card {
    val cardId = extractCardId()
    val winning = extractWinningNumbers()
    val given = extractGivenNumbers()

    return Card(cardId, winning, given)
}

private fun Card.points(): Int {
    var points = 0

    given.forEach {
        if (it in winning) {
            if (points == 0) {
                points = 1
            } else {
                points *= 2
            }
        }
    }

    return points
}

private fun List<Card>.populate(): List<Card> {
    val copies = associate {
        it to mutableListOf<Card>()
    }.toMutableMap()

    forEachIndexed { index, card ->
        val count = card.count()

        (1..count).forEach { currentCount ->
            val cardToCopy = getOrNull(index + currentCount) ?: return@forEach
            val currentCardCopies = copies[card] ?: return@forEach

            repeat(currentCardCopies.size + 1) {
                copies[cardToCopy]?.add(cardToCopy)
            }
        }
    }

    val cards =
        copies.flatMap { (card, copies) ->
            listOf(card, *copies.toTypedArray())
        }

    return cards
}

private fun Card.count() =
    given.count {
        it in winning
    }

private fun String.extractCardId() =
    removePrefix(
        find("Card\\s+".toRegex())
    ).removeSuffix(
        find(":.*".toRegex())
    ).toInt()

private fun String.extractWinningNumbers() =
    extractNumbers().first

private fun String.extractGivenNumbers() =
    extractNumbers().second

private fun String.extractNumbers(): Pair<List<Int>, List<Int>> =
    extractNumbersString().let { numbersString ->
        "[\\d\\s]+".toRegex().findAll(numbersString).map { part ->
            part.value.trim().split("\\s+".toRegex()).map {
                it.toInt()
            }
        }.let {
            it.first() to it.last()
        }
    }

private fun String.extractNumbersString() =
    removePrefix(
        find("Card\\s+\\d+:\\s+".toRegex())
    )

private fun String.find(regex: Regex) =
    requireNotNull(
        regex.find(this)?.value
    )