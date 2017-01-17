package com.github.pitzcarraldo

import java.nio.charset.Charset
import javax.servlet.Filter

import com.github.pitzcarraldo.spring.view.node.{NodeView, NodeViewResolver}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.{HttpMessageConverter, StringHttpMessageConverter}
import org.springframework.web.filter.CharacterEncodingFilter
import org.springframework.web.servlet.ViewResolver

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
@SpringBootApplication
class SpringBootReactUniversalExampleApplication
object SpringBootReactUniversalExampleApplication {
  def main(args: Array[String]) {
    SpringApplication.run(classOf[SpringBootReactUniversalExampleApplication], args: _*)
  }

  @Bean
  def responseBodyConverter(): HttpMessageConverter[String] = new StringHttpMessageConverter(Charset.forName("UTF-8"))

  @Bean
  def characterEncodingFilter(): Filter = {
    val characterEncodingFilter = new CharacterEncodingFilter
    characterEncodingFilter.setEncoding("UTF-8")
    characterEncodingFilter.setForceEncoding(true)
    characterEncodingFilter
  }

  @Bean
  def getViewResolver: ViewResolver = {
    val resolver = new NodeViewResolver
    resolver.setPrefix("/WEB-INF/views/")
    resolver.setSuffix(".js")
    resolver.setViewClass(classOf[NodeView])
    resolver.setContentType("text/html;charset=UTF-8")
    resolver.setCache(false)
    resolver
  }
}
