package com.github.pitzcarraldo.configuration

import java.util
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.base.Objects
import com.google.common.collect.Maps
import org.springframework.web.servlet.view.AbstractTemplateView

import scala.beans.BeanProperty

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeView extends AbstractTemplateView {
  @BeanProperty
  var viewPath: String = _
  var renderer: NodeViewRenderer = new NodeViewRenderer

  override def renderMergedTemplateModel(
                                          model: util.Map[String, AnyRef],
                                          httpRequest: HttpServletRequest,
                                          httpResponse: HttpServletResponse): Unit = {
    httpResponse.setContentType(getContentType)
    model.remove("springMacroRequestContext")
    val writer = httpResponse.getWriter
    try {
      val template: NodeViewTemplate = new NodeViewTemplate("", Maps.newConcurrentMap())
      val viewFilePath = getServletContext.getResource(viewPath).getPath
      val response: util.Map[String, AnyRef] = renderer.render(viewFilePath, model)
      if (response.containsKey("headers")) {
        val headers = response.get("headers").asInstanceOf[util.Map[String, String]]
        headers.forEach((key, value) => httpResponse.addHeader(key, value))
      }
      writer.append(response.get("body").asInstanceOf[String])
    } catch {
      case e: Exception =>
        throw e
    } finally {
      writer.flush()
    }
  }
}

class NodeViewTemplate(@BeanProperty path: String, @BeanProperty model: util.Map[String, AnyRef]) {
  private val mapper = new ObjectMapper

  def modelAsJSON: String = mapper.writeValueAsString(model)

  override def hashCode(): Integer = 31 * Objects.hashCode(path) + Objects.hashCode(modelAsJSON)

  override def equals(o: Any): Boolean = o match {
    case template: NodeViewTemplate => Objects.equal(path, template.path) && Objects.equal(modelAsJSON, template.modelAsJSON)
    case _ => false
  }
}

//object NodeViewCache {
//  private val defaultMaximumCacheSize = 100
//  private val cache = CacheBuilder.newBuilder()
//    .maximumSize(defaultMaximumCacheSize)
//    .expireAfterAccess()
//    .build(new CacheLoader[String, util.Map[String, AnyRef]]() {
//      override def load(path: String): util.Map[String, AnyRef] = render(path)
//    })
//
//  def get(path: String): V8Function = {
//    require(path)
//  }
//
//  def getFromCache(path: String): V8Function = {
//    cache.get(path)
//  }
//}