package model

import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlValue

@XmlAccessorType(XmlAccessType.FIELD)
data class Skill(
    @field:XmlAttribute(name = "hard")
    var hard: Boolean? = null,

    @field:XmlAttribute(name = "soft")
    var soft: Boolean? = null,

    @field:XmlValue
    var name: String = ""
)