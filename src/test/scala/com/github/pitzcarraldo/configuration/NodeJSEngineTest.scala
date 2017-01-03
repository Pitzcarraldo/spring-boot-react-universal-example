package com.github.pitzcarraldo.configuration

import org.scalatest.FunSuite
import java.util
import collection.JavaConverters


/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
class NodeJSEngineTest extends FunSuite {
  val sut = new NodeJSEngine

  def getPath(filename: String): String = getClass.getClassLoader.getResource(filename).getPath

  test("testSyncRender") {
    val mockModel: util.Map[String, AnyRef] = JavaConverters.mapAsJavaMap(Map("type" -> "sync"))
    assert(sut.render(getPath("syncRender.js"), mockModel) == "{\"type\":\"sync\"}")
  }

  test("testAsyncRender") {
    val mockModel: util.Map[String, AnyRef] = JavaConverters.mapAsJavaMap(Map("type" -> "async"))
    assert(sut.render(getPath("asyncRender.js"), mockModel) == "{\"type\":\"async\"}")
  }
}
