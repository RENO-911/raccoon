package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.Survey

fun exampleSurvey(): Survey {
    return Survey {
        defaultValidators = listOf(

        )
        val setA = set {
            val question1 = multipleChoiceQuestion {
                id = "1"
                title = "What is your favorite food"
                options = setOf(
                    "Chocolate".asAnswer(),
                    "Potato's".asAnswer(),
                    "Oranges".asAnswer()
                )
                defaultAnswer = "Chocolate".asAnswer()
                validWhen = { answer != null && answer in options }
            }

            val question2 = multipleChoiceQuestion {
                id = "2"
                title = "Would you tell us why this is your favorite food?"
                options = setOf(
                    "yes".asAnswer(),
                    "no".asAnswer()
                )
                releventWhen = { question1.isValid }
                validWhen = { answer != null && answer in options }
            }

            val question3 = openQuestion {
                id = "3"
                title = "Why is this your favorite food?"
                releventWhen = { question2.isValid && question2.answer?.value == "yes"}
                validWhen = { (answer?.value?.length ?: 0) > 5 }
            }

            val question4 = multiSelectQuestion {
                id = "4"
                title = "Any other foods you like?"
                options = setOf("Beans".asAnswer(), "Rice".asAnswer(), "Dogfood".asAnswer(), "Pancakes".asAnswer())
                releventWhen = { question3.isValid }
            }
        }
        set {
            val setBQuestion1 = openQuestion {
                id = "B1"
                title = "How would you describe your favorite color?"
                releventWhen = { setA.isValid }
                validWhen = { answer?.value in listOf("Red", "Green", "Blue") }
            }

            val setBQuestion2 = multipleChoiceQuestion {
                id = "B2"
                title = "Are you lying?"
                options = setOf(
                    AnswerString("Yes", mapOf("userClaimsLyingCode" to "LIAR")),
                    AnswerString("No", mapOf("userClaimsLyingCode" to "TRUTHFUL"))
                )
                releventWhen = { setBQuestion1.isValid }
            }

            val setBQuestion3 = photoQuestion {
                id = "B3"
                title = "Can you show us your favorite color?"
                releventWhen = { setBQuestion2.isValid }
            }
        }
    }
}

val singlePhotoQuestionSurvey = Survey {
    set {
        photoQuestion {
            id = "1"
            title = "Can you show us your favorite color?"
        }
    }
}