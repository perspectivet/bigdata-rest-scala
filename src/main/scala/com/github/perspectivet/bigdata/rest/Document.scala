package com.github.perspectivet.bigdata.rest

import scala.reflect._
import grizzled.slf4j.Logger
import java.util.{List => JList}
import scala.collection.JavaConverters._

import org.openrdf.model.{URI,Value,Resource}
@BeanInfo
class PredicateObject(@BeanProperty var pred:URI, 
		      @BeanProperty var obj:Value) {
  
  override def toString():String = {
    pred.stringValue + ":" + obj.stringValue
  }
}

@BeanInfo
class Document(@BeanProperty var subject:Resource,
	       @BeanProperty var properties:JList[PredicateObject]) {
  override def toString():String = {
    properties.asScala.map { _.toString } mkString(subject.stringValue + " : {\n\t","\n\t","}")
  }
}

