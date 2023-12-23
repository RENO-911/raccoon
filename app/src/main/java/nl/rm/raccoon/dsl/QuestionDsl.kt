package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.MultiSelectQuestion
import nl.rm.raccoon.domain.MultipleChoiceQuestion
import nl.rm.raccoon.domain.OpenQuestion
import nl.rm.raccoon.domain.Question

class MultipleChoiceQuestionBuilder() {
    lateinit var id: String
    lateinit var title: String
    lateinit var options: Set<String>
    var defaultAnswer: String? = null
    var releventWhen: () -> Boolean = { true }
    lateinit var validWhen: Question.() -> Boolean
}

fun QuestionSetBuilder?.multipleChoiceQuestion(
    init: MultipleChoiceQuestionBuilder.() -> Unit
): MultipleChoiceQuestion {
    val builder = MultipleChoiceQuestionBuilder()

    with(builder) {
        init()
        val build = MultipleChoiceQuestion(
            id = id,
            title = title,
            options = options
        )

        defaultAnswer?.let { build.answer(it) }

        this@multipleChoiceQuestion?.addQuestion(build)

        return build
    }
}

class OpenQuestionBuilder() {
    lateinit var id: String
    lateinit var title: String
    var defaultAnswer: String? = null
    var releventWhen: () -> Boolean = { true }
    lateinit var validWhen: Question.() -> Boolean
}

fun QuestionSetBuilder?.openQuestion(
    init: OpenQuestionBuilder.() -> Unit
): OpenQuestion {
    val builder = OpenQuestionBuilder()

    with(builder) {
         init()

         val build = OpenQuestion(
             id = id,
             title = title,
             isRelevant = releventWhen,
             isValidWhen = validWhen
         )

        defaultAnswer?.let { build.answer(it) }

        this@openQuestion?.addQuestion(build)

        return build
    }
}

class MultiSelectQuestionBuilder() {
    lateinit var id: String
    lateinit var title: String
    lateinit var options: Set<String>
    var defaultAnswer: String? = null
    var releventWhen: () -> Boolean = { true }
    lateinit var validWhen: Question.() -> Boolean
}

fun QuestionSetBuilder?.multiSelectQuestion(
    init: MultiSelectQuestionBuilder.() -> Unit
): MultiSelectQuestion {
    val builder = MultiSelectQuestionBuilder()

    with(builder) {
        init()

        val build = MultiSelectQuestion(
            id = id,
            title = title,
            options = options,
            isRelevant = releventWhen
        )

        defaultAnswer?.let { build.answer(it) }

        this@multiSelectQuestion?.addQuestion(build)

        return build
    }
}