package com.github.perspectivet.bigdata.rest

import scala.reflect._
import grizzled.slf4j.Logger
import java.util.{List => JList}
import scala.collection.JavaConverters._

@BeanInfo
class PredicateObject(@BeanProperty var pred:String, 
		      @BeanProperty var obj:String) {
  
  override def toString():String = {
    pred + ":" + obj
  }
}

@BeanInfo
class Document(@BeanProperty var subject:String,
	       @BeanProperty var properties:JList[PredicateObject]) {
  override def toString():String = {
    properties.asScala.map { _.toString } mkString(subject + " : {\n\t","\n\t","}")
  }
}

object Document {
/*  def from(subj:String,resultSet:ResultSet):Document = {
    val po = resultSet.rows map {
      b => {
	new PredicateObject(b.head, b.tail.head)
      } 
    }
    new Document(subj,po)
  }
  */
  def toNTriplesAddSet(a1:Document, a2:Document):Option[String] = {
    if(a1.subject != a2.subject) {
      None
    } else {
      val preAdd = a2.properties.asScala.toSet
      val toAdd = preAdd diff a1.properties.asScala.toSet
      Some(toAdd map { a => "%s %s %s" format (a1.subject,a.pred,a.obj) } mkString(".\n"))
    }
  }

  def toNTriplesDeleteSet(a1:Document, a2:Document):Option[String] = {
    if(a1.subject != a2.subject) {
      None
    } else {
      val preDelete = a1.properties.asScala.toSet
      val toDelete = preDelete diff a2.properties.asScala.toSet
      Some(toDelete map { a => "%s %s %s" format (a1.subject,a.pred,a.obj) } mkString(".\n"))
    }
  }
}
