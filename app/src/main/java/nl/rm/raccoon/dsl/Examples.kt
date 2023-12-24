package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.Survey

fun exampleSurvey(): Survey {
    return Survey {
        val setA = set {
            val question1 = multipleChoiceQuestion {
                id = "1"
                title = "What is your favorite food"
                options = setOf("Chocolate", "Potato's", "Oranges")
                defaultAnswer = "Chocolate"
                validWhen = { answer != null && answer in options }
            }

            val question2 = multipleChoiceQuestion {
                id = "2"
                title = "Would you tell us why this is your favorite food?"
                options = setOf("yes", "no")
                releventWhen = { question1.isValid }
                validWhen = { answer != null && answer in options }
            }

            val question3 = openQuestion {
                id = "3"
                title = "Why is this your favorite food?"
                releventWhen = { question2.isValid && question2.answer == "yes"}
                validWhen = { (answer?.length ?: 0) > 50 }
            }

            val question4 = multiSelectQuestion {
                id = "4"
                title = "Any other foods you like?"
                options = setOf("Beans", "Rice", "Dogfood", "Pancakes")
                releventWhen = { question3.isValid }
            }
        }
        set {
            val setBQuestion1 = openQuestion {
                id = "B1"
                title = "How would you describe your favorite color?"
                releventWhen = { setA.isValid }
                validWhen = { answer in listOf("Red", "Green", "Blue") }
            }

            val setBQuestion2 = multipleChoiceQuestion {
                id = "B2"
                title = "Are you lying?"
                options = setOf("Yes", "No")
                releventWhen = { setBQuestion1.isValid }
            }
        }
    }
}