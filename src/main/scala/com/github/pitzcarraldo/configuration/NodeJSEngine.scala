package com.github.pitzcarraldo.configuration

import java.io.File
import java.util

import com.eclipsesource.v8._
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.cache.{LoadingCache, CacheBuilder, CacheLoader}

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeJSEngine {
  private val mapper: ObjectMapper = new ObjectMapper
  private val defaultMaximumCacheSize = 100
  private val cache: LoadingCache[String, File] = CacheBuilder.newBuilder()
    .maximumSize(defaultMaximumCacheSize)
    .build(new CacheLoader[String, File]() {
      override def load(path: String): File = new File(path)
    })

  def render(viewPath: String, model: util.Map[String, AnyRef]): String = {
    val nodeJS: NodeJS = NodeJS.createNodeJS(null)
    val render: V8Function = nodeJS.require(new File(viewPath)).asInstanceOf[V8Function]
    val args: V8Array = new V8Array(render.getRuntime)
    val v8Model = toV8Value(model, render.getRuntime)
    var asyncResult: String = ""
    val callback = new V8Function(render.getRuntime, (receiver: V8Object, parameters: V8Array) => {
      asyncResult = getResult(parameters.get(0))
      receiver.release()
      parameters.release()
      null
    })
    args.push(v8Model)
    args.push(callback)
    val syncResult = getResult(render.call(null, args))
    while (nodeJS.isRunning) {
      nodeJS.handleMessage
    }
    v8Model.release()
    callback.release()
    args.release()
    render.release()
    nodeJS.release()
    if (!asyncResult.isEmpty) asyncResult else syncResult
  }

  private def toV8Value(`object`: AnyRef, runtime: V8): V8Value = {
    val json = runtime.getObject("JSON")
    val parameters = new V8Array(runtime).push(mapper.writeValueAsString(`object`))
    val result = json.executeObjectFunction("parse", parameters)
    parameters.release()
    json.release()
    result
  }

  private def getResult(returnValue: AnyRef): String = {
    var result: String = ""
    returnValue match {
      case value: String => result = value
      case value: V8Value =>
        if (!value.isUndefined) {
          result = value.toString
        }
        value.release()
      case _ =>
    }
    result
  }
}