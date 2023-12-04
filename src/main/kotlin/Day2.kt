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

private const val RED_LIMIT = 12
private const val GREEN_LIMIT = 13
private const val BLUE_LIMIT = 14

private data class Game(
    val id: Int,
    val handfuls: List<Handful>
)

private data class Handful(
    val red: Int,
    val green: Int,
    val blue: Int
)

private val lines
    get() = INPUT.lines()
        .map { it.trim() }
        .filter { it.isNotEmpty() }

fun day2() {
    println("Running AoC Day 2 (1st part)...")
    firstPart()

    println()

    println("Running AoC Day 2 (2nd part)...")
    secondPart()
}

private fun firstPart() {
    val games = lines.parseGames()

    val res = games.sumOf { game ->
        val beyondLimit = game.handfuls.any {
            it.isBeyondLimit()
        }

        if (beyondLimit) 0 else game.id
    }

    println("res: $res")
}

private fun secondPart() {
    val games = lines.parseGames()

    val res = games.sumOf { game ->
        val handfuls = game.handfuls

        val minRed = handfuls.maxOf {
            it.red
        }

        val minGreen = handfuls.maxOf {
            it.green
        }

        val minBlue = handfuls.maxOf {
            it.blue
        }

        minRed * minGreen * minBlue
    }

    println("res: $res")
}

private fun List<String>.parseGames() =
    map {
        it.parseGame()
    }

private fun String.parseGame(): Game {
    val id = findId()

    val handfuls = findHandfulsString()
        .findHandfulDeclarations()
        .map { handfulString ->
            var red = 0
            var green = 0
            var blue = 0

            handfulString.findCubeStrings().forEach { cubeString ->
                val countAndColor = cubeString.split(" ")
                val count = countAndColor.first().toInt()
                val color = countAndColor.last()

                when (color) {
                    "red" -> red += count
                    "green" -> green += count
                    "blue" -> blue += count
                }
            }

            Handful(red, green, blue)
        }

    return Game(id, handfuls)
}

private fun String.findId() =
    requireNotNull("(?<=Game\\s)\\d+".toRegex().find(this)).value.toInt()

private fun String.findHandfulsString() =
    replace("Game\\s\\d+:\\s".toRegex(), "")

private fun String.findHandfulDeclarations() =
    split("; ")

private fun String.findCubeStrings() =
    split(", ")

private fun Handful.isBeyondLimit() =
    red > RED_LIMIT || green > GREEN_LIMIT || blue > BLUE_LIMIT