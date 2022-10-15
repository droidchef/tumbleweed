package io.redgreen.tumbleweed

import java.lang.StringBuilder

@JvmInline
value class TypeToken(private val descriptor: String) {
  companion object {
    fun isPrimitive(char: String): Boolean =
      char in listOf("B", "C", "D", "F", "I", "J", "S", "Z", "V")

    fun isObject(char: String): Boolean =
      char == "L"

    fun isArray(char: String): Boolean =
      char == "["
  }

  val type: String
    get() = if (descriptor.length == 1) {
      primitiveType(descriptor)
    } else {
      println("Unsanitized TYPE = {$descriptor}")
      val result = simpleArrayOrObjectType(descriptor)
      println("TYPE = {$result}")
      result
    }

  private fun primitiveType(descriptor: String): String {
    return when (descriptor) {
      "V" -> "void"
      "Z" -> "boolean"
      "B" -> "byte"
      "S" -> "short"
      "C" -> "char"
      "I" -> "int"
      "J" -> "long"
      "F" -> "float"
      "D" -> "double"
      else -> {
        throw IllegalArgumentException("Unknown type: $descriptor")
      }
    }
  }


  /*
  This method returns Simple Type name
  For Example
    - android.context.Context would be Context
    - java.util.List would be List
   */
  private fun simpleArrayOrObjectType(descriptor: String): String {
    val sanitizedType = sanitizeDescriptor(descriptor)

    var processedType = if (sanitizedType.startsWith("[L")) {
      "[]${sanitizedType.drop(2)}"
    } else if (sanitizedType.startsWith("[")) {
      "[]${primitiveType(sanitizedType.drop(1))}"
    } else {
      sanitizedType.removePrefix("L")
    }

    val types = processedType.split(";")

    if (types.size == 1) {
      return types.first().splitToSequence(".").last()
    } else {
      println("MULTI TYPE ---> $types")
    }

    val sb = StringBuilder()
    var depth = 0
    types.forEach {
      if (sb.isBlank()) {
        sb.append(it.splitToSequence(".").last())
      } else {
        sb.append("<")
        depth++
        sb.append(it.splitToSequence(".").last())
      }
    }

    repeat(depth) {
      sb.append(">")
    }

    return sb.toString()

  }

  private fun sanitizeDescriptor(descriptor: String): String {
    return descriptor
      .removeSuffix(";")
      .replace("/", ".")
  }

  /*
    This method returns Fully Qualified Type name
    For Example
    - android.context.Context would be returned as is
    - java.util.List would be returned as is

   */
  private fun arrayOrObjectType(descriptor: String): String {
    val sanitizedType = sanitizeDescriptor(descriptor)

    return if (sanitizedType.startsWith("[L")) {
      "[]${sanitizedType.drop(2)}"
    } else if (sanitizedType.startsWith("[")) {
      "[]${primitiveType(sanitizedType.drop(1))}"
    } else {
      sanitizedType.removePrefix("L")
    }
  }
}
