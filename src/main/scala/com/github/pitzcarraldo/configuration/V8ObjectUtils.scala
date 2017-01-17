package com.github.pitzcarraldo.configuration

import java.util

import com.eclipsesource.v8.{V8, V8Array, V8Object, V8Value}
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.{ImmutableMap, Maps}

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class V8ObjectUtils(runtime: V8) {
  private val mapper= new ObjectMapper

  object JSON {
    def parse(string: String): V8Object = {
      val json = runtime.getObject("JSON")
      val parameters = new V8Array(runtime).push(string)
      val result = json.executeObjectFunction("parse", parameters)
      parameters.release()
      json.release()
      result
    }

    def stringify(`object`: V8Value): String = {
      val json = runtime.getObject("JSON")
      val parameters = new V8Array(runtime).push(`object`)
      val result = json.executeStringFunction("stringify", parameters)
      parameters.release()
      json.release()
      result
    }
  }

  def fromMap(model: util.Map[String, AnyRef]): V8Object = {
    JSON.parse(mapper.writeValueAsString(model))
  }

  def toMap(response: V8Object): util.Map[String, String] = {
    mapper.readValue(JSON.stringify(response), classOf[util.Map[String, String]])
  }

  def toResponse(value: AnyRef): util.Map[String, AnyRef] = value match {
    case value: String => ImmutableMap.of("body", value)
    case value: V8Object =>
      val builder = new ImmutableMap.Builder[String, AnyRef]()
      if (!value.isUndefined) {
        if (value.contains("headers")) {
          val headers = value.getObject("headers")
          builder.put("headers", toMap(headers))
          headers.release()
        }
        if (value.contains("body")) {
          builder.put("body", value.getString("body"))
        }
      }
      value.release()
      builder.build()
    case _ => ImmutableMap.of("body", "")
  }
}