package com.github.perspectivet.bigdata.rest

case class Head(val vars:List[String])
case class Result(val `type`:String, val datatype:Option[String], val value:String)
case class Bindings(val bindings:List[Map[String,Result]])
case class ResultSet(val head:Head, val results:Bindings) {
  def columnNames = head.vars
  def rows = results.bindings
}
