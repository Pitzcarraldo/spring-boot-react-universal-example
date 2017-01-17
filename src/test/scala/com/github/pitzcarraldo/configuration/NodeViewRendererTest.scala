package com.github.pitzcarraldo.configuration

import java.util

import com.google.common.collect.{ImmutableMap, Maps}
import org.fest.assertions.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
@RunWith(classOf[SpringRunner])
@SpringBootTest
class NodeViewRendererTest {
  val sut = new NodeViewRenderer

  def getPath(filename: String): String = getClass.getClassLoader.getResource(filename).getPath

  @Test
  def testSyncRender(): Unit = {
    // given
    val model: util.Map[String, AnyRef] = ImmutableMap.of("type", "sync")

    // when
    var actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)
    actual = sut.render(getPath("syncRender.js"), model)

    // then
    val expected = "{\"type\":\"sync\"}"
    assertThat(actual.get("body")).isEqualTo(expected)
  }

  @Test
  def testSyncRenderWithResponse(): Unit = {
    // given
    val model: util.Map[String, AnyRef] = ImmutableMap.of("type", "sync")

    // when
    val actual = sut.render(getPath("syncRenderWithResponse.js"), model)
    val headers = actual.get("headers").asInstanceOf[util.Map[String, String]]
    val body = actual.get("body").asInstanceOf[String]

    // then
    val expectedContentType = "application/json"
    val expectedBody = "{\"type\":\"sync\"}"
    assertThat(headers.get("Content-Type")).isEqualTo(expectedContentType)
    assertThat(body).isEqualTo(expectedBody)
  }

  @Test
  def testAsyncRender(): Unit = {
    // given
    val model: util.Map[String, AnyRef] = ImmutableMap.of("type", "async")

    // when
    val actual = sut.render(getPath("asyncRender.js"), model)

    // then
    val expected = "{\"type\":\"async\"}"
    assertThat(actual.get("body")).isEqualTo(expected)
  }

  @Test
  def testAsyncRenderWithResponse(): Unit = {
    // given
    val model: util.Map[String, AnyRef] = ImmutableMap.of("type", "async")

    // when
    val actual = sut.render(getPath("asyncRenderWithResponse.js"), model)
    val headers = actual.get("headers").asInstanceOf[util.Map[String, String]]
    val body = actual.get("body").asInstanceOf[String]

    // then
    val expectedContentType = "application/json"
    val expectedBody = "{\"type\":\"async\"}"
    assertThat(headers.get("Content-Type")).isEqualTo(expectedContentType)
    assertThat(body).isEqualTo(expectedBody)

  }
}
