package com.github.pitzcarraldo

import com.github.pitzcarraldo.spring.view.node.{NodeView, NodeViewResolver}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.web.servlet.ViewResolver
import org.springframework.web.servlet.config.annotation.{DefaultServletHandlerConfigurer, EnableWebMvc, WebMvcConfigurerAdapter}

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
@SpringBootApplication
class SpringBootReactUniversalExampleApplication
object SpringBootReactUniversalExampleApplication extends WebMvcConfigurerAdapter {
  def main(args: Array[String]) {
    SpringApplication.run(classOf[SpringBootReactUniversalExampleApplication], args: _*)
  }
}

@Configuration
@EnableWebMvc
class MvcConfig extends WebMvcConfigurerAdapter {
  @Bean
  def getViewResolver: ViewResolver = {
    val resolver = new NodeViewResolver
    resolver.setPrefix("/WEB-INF/views/")
    resolver.setSuffix(".js")
    resolver.setViewClass(classOf[NodeView])
    resolver.setCache(false)
    resolver
  }

  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer): Unit =
    configurer.enable()
}