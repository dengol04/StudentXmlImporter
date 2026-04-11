package util

import jakarta.xml.bind.JAXBContext
import jakarta.xml.bind.Marshaller
import model.JaxbSerializable
import java.io.File
import java.io.InputStream

object JaxbContextCache {
    private val cache = mutableMapOf<Class<*>, JAXBContext>()

    operator fun get(targetClass: Class<*>): JAXBContext =
        cache.getOrPut(targetClass) { JAXBContext.newInstance(targetClass) }
}

inline fun <reified T> InputStream.unmarshalXml(): T {
    val context = JaxbContextCache[T::class.java]
    val unmarshaller = context.createUnmarshaller()
    return T::class.java.cast(unmarshaller.unmarshal(this))
}

inline fun <reified T : JaxbSerializable> T.marshalXml(file: File) {
    val context = JaxbContextCache[T::class.java]
    val marshaller = context.createMarshaller()
    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true)
    marshaller.marshal(this, file)
}