import jakarta.xml.bind.annotation.XmlAccessType
import jakarta.xml.bind.annotation.XmlAccessorType
import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlValue

@XmlAccessorType(XmlAccessType.FIELD)
data class Skill(
    @field:XmlAttribute(name = "hard")
    var hard: Boolean = false,

    @field:XmlAttribute(name = "soft")
    var soft: Boolean = false,

    @field:XmlValue
    var name: String = ""
)