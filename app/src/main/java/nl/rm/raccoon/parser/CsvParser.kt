package nl.rm.raccoon.parser

import nl.rm.raccoon.domain.MultipleChoiceQuestion
import nl.rm.raccoon.domain.Question
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class CsvParser {
}

fun readCsv(inputStream: InputStream){
    val inputStreamReader = FileInputStream(File(""))
        .bufferedReader()

    val answerColumnRange = 1..3
    val lines = inputStreamReader.readLines().map {
        it.split(",")
    }

    val initialQuestion = MultipleChoiceQuestion(
        id = "1",
        title = "Wat is je favoriete kleur?",
        options = getInitialAnswers(lines)
    )
}

fun parseLine(currentColumn: Int, line: List<String>) {
}

fun getAnswers(items: List<List<String>>) {
    val initialQuestion = MultipleChoiceQuestion(
        id = "1",
        title = "Wat is je favoriete kleur?",
        options = getInitialAnswers(items)
    )


}

fun getInitialAnswers(items: List<List<String>>) =
    items
        .map { item -> item[1] }
        .toSet()

fun getFollowUpAnswers(items: List<List<String>>, previousAnswer: String, previousAnswerColumn: Int) =
    items
        .filter { item -> item[previousAnswerColumn] == previousAnswer }
        .map { item -> item[previousAnswerColumn + 1] }
        .toSet()

