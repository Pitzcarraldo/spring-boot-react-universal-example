package com.github.pitzcarraldo.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.{RequestMapping, ResponseBody}

/**
  * @author Minkyu Cho (https://github.com/Pitzcarraldo)
  */
@Controller
class IndexController {
  @RequestMapping(Array("/hello"))
  def hello(model: Model): String = {
    model.addAttribute("key", "value")
    "hello"
  }

  @RequestMapping(Array("/"))
  @ResponseBody
  def index(model: Model): String = {
    "hello"
  }
}
