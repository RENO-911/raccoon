package nl.rm.raccoon.dsl

import nl.rm.raccoon.domain.AnswerString
import nl.rm.raccoon.domain.LambdaValidator
import nl.rm.raccoon.domain.Survey
import nl.rm.raccoon.domain.Validators
import nl.rm.raccoon.domain.validators

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
                validWhen = validators(
                    LambdaValidator(
                        message = "Please select one of the available options",
                        validator = {
                            options.contains(answer)
                        }
                    )
                )
            }

            val question2 = multipleChoiceQuestion {
                id = "2"
                title = "Would you tell us why this is your favorite food?"
                options = setOf(
                    "yes".asAnswer(),
                    "no".asAnswer()
                )
                releventWhen = { question1.isValid.isValid }
                validWhen = LambdaValidator(
                    message = "Please select one of the available options",
                    validator = {
                        options.contains(answer)
                    }
                )
            }

            val question3 = openQuestion {
                id = "3"
                title = "Why is this your favorite food?"
                releventWhen = { question2.isValid.isValid && question2.answer?.value == "yes"}
                validWhen = validators(
                    LambdaValidator(
                        message = "Can't be blank",
                        validator = {
                            answer?.value?.isNotBlank() ?: false
                        }
                    ),
                    LambdaValidator(
                        message = "Can't be longer than 25 characters",
                        validator = {
                            (answer?.value?.length ?: 0) <= 25
                        }
                    ),
                    LambdaValidator(
                        message = "Can't be shorter than 5 characters",
                        validator = {
                            (answer?.value?.length ?: 0) >= 5
                        }
                    )
                )
            }

            val question4 = multiSelectQuestion {
                id = "4"
                title = "Any other foods you like?"
                options = setOf("Beans".asAnswer(), "Rice".asAnswer(), "Dogfood".asAnswer(), "Pancakes".asAnswer())
                releventWhen = { question3.isValid.isValid }
            }
        }
        set {
            val setBQuestion1 = openQuestion {
                id = "B1"
                title = "How would you describe your favorite color? (valid answers: Red, Green, Blue)"
                releventWhen = { setA.isValid }
                //validWhen = { answer?.value in listOf("Red", "Green", "Blue") }
            }

            val setBQuestion2 = multipleChoiceQuestion {
                id = "B2"
                title = "Are you lying?"
                options = setOf(
                    AnswerString("Yes", mapOf("userClaimsLyingCode" to "LIAR")),
                    AnswerString("No", mapOf("userClaimsLyingCode" to "TRUTHFUL"))
                )
                releventWhen = { setBQuestion1.isValid.isValid }
            }

            val setBQuestion3 = photoQuestion {
                id = "B3"
                title = "Can you show us your favorite color?"
                releventWhen = { setBQuestion2.isValid.isValid }
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