package com.github.pitzcarraldo

import com.github.pitzcarraldo.configuration.{NodeJSView, NodeJSViewResolver}
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
    val resolver = new NodeJSViewResolver
    resolver.setPrefix("/WEB-INF/views/")
    resolver.setSuffix(".js")
    resolver.setViewClass(classOf[NodeJSView])
    resolver
  }

  override def configureDefaultServletHandling(configurer: DefaultServletHandlerConfigurer): Unit =
    configurer.enable()
}