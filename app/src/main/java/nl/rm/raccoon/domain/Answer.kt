package nl.rm.raccoon.domain

interface Answer : IProperties {
    val value: String

    override fun equals(other: Any?): Boolean
}

abstract class AnswerImpl(
    override val value: String,
    override val properties: Map<String, *> = emptyMap<String, Any>()
) : Answer {
    override fun equals(other: Any?): Boolean {
        return other is Answer && other.value == value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

class AnswerString(
    override val value: String,
    override val properties: Map<String, *> = emptyMap<String, Any>()
) : AnswerImpl(value, properties)