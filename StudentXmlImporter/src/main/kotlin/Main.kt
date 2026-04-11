import model.Skill
import model.StudentList
import repository.StudentsRepository
import util.marshalXml
import util.unmarshalXml
import java.io.File

fun main() {
    val inputStream = object {}.javaClass.getResourceAsStream("/students.xml")
        ?: error("/students.xml не найден в папке resources")

    val students: StudentList = inputStream.unmarshalXml()

    val newSkills = listOf(
        Skill(hard = true, name = "C++"),
        Skill(hard = true, name = "Docker"),
        Skill(soft = true, name = "Analytical thinking")
    )

    for (student in students.students) {
        student.skills.add(newSkills.random())
    }

    println(students)

    students.marshalXml(File("./src/main/resources/newStudents.xml"))

    val dbHost = System.getenv("DB_HOST")
    val dbPort = System.getenv("DB_PORT")
    val dbName = System.getenv("DB_NAME")
    val dbUser = System.getenv("DB_USER")
    val dbPassword = System.getenv("DB_PASSWORD")

    val jdbcUrl = "jdbc:postgresql://$dbHost:$dbPort/$dbName"

    val rep = StudentsRepository(jdbcUrl, dbUser, dbPassword)

    rep.saveParsedData(students)

}