package com.github.pitzcarraldo.configuration

import org.springframework.beans.factory.InitializingBean
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.view.{AbstractTemplateViewResolver, AbstractUrlBasedView}

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeViewResolver extends AbstractTemplateViewResolver with ViewResolver with InitializingBean {
  override protected def buildView(viewName: String): AbstractUrlBasedView = {
    val view: NodeView = super.buildView(viewName).asInstanceOf[NodeView]
    try {
      view.setViewPath(getPrefix + viewName + getSuffix)
      view
    } catch {
      case e: Exception => throw e
    }
  }

  override def afterPropertiesSet(): Unit = {}

  override protected def requiredViewClass: Class[_] = classOf[NodeView]

}
