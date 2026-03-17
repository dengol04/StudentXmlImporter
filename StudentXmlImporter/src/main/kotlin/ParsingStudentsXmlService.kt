import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import java.io.File
import java.io.InputStream

object ParsingStudentsXmlService {
    private val jaxbContext = JAXBContext.newInstance(StudentList::class.java)
    
    fun unmarshalStudents(inputStream: InputStream): StudentList {
        val unmarshaller = jaxbContext.createUnmarshaller()
        return unmarshaller.unmarshal(inputStream) as StudentList
    }
    
    fun marshalStudents(students: StudentList, outputFile: File) {
        val marshaller = jaxbContext.createMarshaller()
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
        marshaller.marshal(students, outputFile)
    }
}