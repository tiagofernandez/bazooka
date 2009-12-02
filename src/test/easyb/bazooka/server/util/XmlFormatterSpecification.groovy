package bazooka.server.util

import static bazooka.server.util.XmlFormatter.format
import static bazooka.server.util.XmlFormatter.isXml

it "should format an xml", {
  unformattedXml = '<foo><bar>abc</bar></foo>'

  formattedXml = format(unformattedXml)
  formattedXml.shouldNotBe unformattedXml

  ensure(formattedXml.indexOf('<?xml version="1.0" encoding="UTF-8"?>') != -1) { isTrue }
}

it "should spot an xml", {
  isXml('<?xml version="1.0" encoding="UTF-8"?><foo/>').shouldBe true
  isXml('<foo/>').shouldBe false
}