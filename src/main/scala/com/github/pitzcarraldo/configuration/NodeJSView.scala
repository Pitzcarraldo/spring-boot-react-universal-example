package com.github.pitzcarraldo.configuration

import java.util
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import org.springframework.web.servlet.support.RequestContext
import org.springframework.web.servlet.view.AbstractTemplateView

import scala.beans.BeanProperty

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeJSView extends AbstractTemplateView {
  @BeanProperty
  var viewPath: String = _
  var engine: NodeJSEngine = new NodeJSEngine

  override def renderMergedTemplateModel(
                                          model: util.Map[String, AnyRef],
                                          request: HttpServletRequest,
                                          response: HttpServletResponse): Unit = {
    response.setContentType(getContentType)
    model.remove("springMacroRequestContext")
    val writer = response.getWriter
    try {
      val viewFilePath = getServletContext.getResource(viewPath).getPath
      val result = engine.render(viewFilePath, model)
      writer.append(result)
    } catch {
      case e: Exception =>
        throw e
    } finally {
      writer.flush()
    }
  }
}
