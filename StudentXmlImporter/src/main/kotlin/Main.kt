import java.io.File
import java.net.URL
import java.nio.file.Path

fun main() {
    val inputStream = object {}.javaClass.getResourceAsStream("/students.xml")
        ?: error("/students.xml не найден в папке resources")

    val students: StudentList = ParsingStudentsXmlService.unmarshalStudents(inputStream)

    println(students)

}