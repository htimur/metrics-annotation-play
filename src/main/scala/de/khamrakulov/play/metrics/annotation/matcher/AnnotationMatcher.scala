package de.khamrakulov.play.metrics.annotation.matcher

import java.lang.annotation.Annotation
import java.lang.reflect.Method

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait AnnotationMatcher {
  def get[T <: Annotation: Manifest](method: Method): Option[T]
}

object ClassAnnotationMatcher {
  def apply() = new ClassAnnotationMatcher()
}

class ClassAnnotationMatcher() extends AnnotationMatcher {
  override def get[T <: Annotation](method: Method)(implicit ev: Manifest[T]): Option[T] = Option {
    method.getDeclaringClass.getAnnotation(ev.runtimeClass.asInstanceOf[Class[T]])
  }
}

object MethodAnnotationMatcher {
  def apply() = new MethodAnnotationMatcher()
}

class MethodAnnotationMatcher() extends AnnotationMatcher {
  override def get[T <: Annotation](method: Method)(implicit ev: Manifest[T]): Option[T] = Option {
    method.getAnnotation(ev.runtimeClass.asInstanceOf[Class[T]])
  }
}