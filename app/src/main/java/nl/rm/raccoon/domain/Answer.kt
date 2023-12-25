package nl.rm.raccoon.domain

interface Answer {
    val value: String
    val properties: Set<NamedProperty<*>>
}

interface NamedProperty<T> {
    val key: String
    val value: T
}

class AnswerString(
    override val value: String,
    override val properties: Set<NamedProperty<*>> = emptySet()
): Answer