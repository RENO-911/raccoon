package nl.rm.raccoon.ui.questions

import nl.rm.raccoon.domain.Answer
import nl.rm.raccoon.domain.Question

internal typealias OnAnswerLambda = (Question, Answer) -> Unit
