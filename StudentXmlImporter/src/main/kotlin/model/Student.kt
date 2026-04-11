package model

import jakarta.xml.bind.annotation.*

@XmlAccessorType(XmlAccessType.FIELD)
data class Student(
    @field:XmlElement(name = "first_name")
    var firstName: String = "",

    @field:XmlElement(name = "second_name")
    var secondName: String = "",

    @field:XmlElementWrapper(name = "skills")
    @field:XmlElement(name = "skill")
    var skills: MutableList<Skill> = mutableListOf()
) {
    override fun toString(): String {
        return "$firstName $secondName, skills: " + skills.joinToString(", ")
    }
}
