package com.github.pitzcarraldo.configuration

import java.io.File
import java.util

import com.eclipsesource.v8._
import com.google.common.collect.Maps

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeViewRenderer extends AutoCloseable {
  private val nodeJS = NodeJS.createNodeJS(null)

  def render(viewPath: String, model: util.Map[String, AnyRef]): util.Map[String, AnyRef] = {
    val render: V8Function = nodeJS.require(new File(viewPath)).asInstanceOf[V8Function]
    val runtime = render.getRuntime
    val objectUtils = new V8ObjectUtils(runtime)
    val v8Model = objectUtils.fromMap(model)
    var response: util.Map[String, AnyRef] = Maps.newHashMap()
    val callback = new V8Function(render.getRuntime, (receiver: V8Object, parameters: V8Array) => {
      response = objectUtils.toResponse(parameters.get(0))
      receiver.release()
      parameters.release()
      null
    })
    val args = new V8Array(runtime)
    args.push(v8Model)
    args.push(callback)
    response = objectUtils.toResponse(render.call(null, args))
    while (nodeJS.isRunning) {
      nodeJS.handleMessage
    }
    v8Model.release()
    callback.release()
    args.release()
    render.release()
    response
  }

  override def close(): Unit = {
    nodeJS.release()
  }
}