package io.redgreen.tumbleweed

import java.io.BufferedInputStream
import java.io.File
import net.bytebuddy.jar.asm.ClassReader
import net.bytebuddy.jar.asm.ClassVisitor
import net.bytebuddy.jar.asm.FieldVisitor
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes.ASM9
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ClassScanner {
  const val ASM_API_VERSION = ASM9

  private val logger: Logger = LoggerFactory.getLogger(ClassScanner::class.java)

  fun scan(classFile: File): ClassStructure {
    logger.debug("Scanning class file: {}", classFile.absolutePath)
    var className: String? = null
    var packageName: String? = null
    val outFields = mutableListOf<Field>()
    val outMethods = mutableListOf<Method>()
    val outRelationships = mutableListOf<Relationship>()
    var topLevelType: String? = null

    val classVisitor = object : ClassVisitor(ASM_API_VERSION) {
      override fun visit(
        version: Int,
        access: Int,
        name: String,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?,
      ) {
        logger.debug("Visiting class: {}", name)

        topLevelType = name
        name.split("/").let { fqClassNameParts ->
          packageName = fqClassNameParts.dropLast(1).joinToString(".")
          className = fqClassNameParts.last()
        }
      }

      override fun visitField(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        value: Any?,
      ): FieldVisitor {
        logger.debug("Visiting field: {}", name)

        outFields.add(Field(name!!, FieldDescriptor.from(descriptor!!)))
        return object : FieldVisitor(ASM_API_VERSION) { /* no-op */ }
      }

      override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?,
      ): MethodVisitor {
        logger.debug("Visiting method: {}", name)

        val method = Method(name!!, MethodDescriptor(descriptor!!))
        outMethods.add(method)
        return InstructionScanner.scan(topLevelType!!, method, outRelationships)
      }
    }

    ClassReader(BufferedInputStream(classFile.inputStream())).accept(classVisitor, 0)

    val allMembersInRelationships = outRelationships.flatMap { listOf(it.source, it.target) }
    val methodsFromRelationships = allMembersInRelationships.filterIsInstance<Method>().distinct()
    val fieldsFromRelationships = allMembersInRelationships.filterIsInstance<Field>().distinct()

    val missingFields = fieldsFromRelationships - outFields.toSet()
    val missingMethods = methodsFromRelationships - outMethods.toSet()

    return ClassStructure(
      packageName!!,
      className!!,
      outFields + missingFields,
      outMethods + missingMethods,
      outRelationships.toList(),
    ).simplify()
  }
}
