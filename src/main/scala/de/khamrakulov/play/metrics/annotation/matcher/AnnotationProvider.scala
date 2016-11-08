package de.khamrakulov.play.metrics.annotation.matcher

import java.lang.annotation.Annotation
import java.lang.reflect.Method

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object AnnotationProvider {
  def apply(matchers: List[AnnotationMatcher]) = new AnnotationProvider(matchers)
}

class AnnotationProvider(matchers: List[AnnotationMatcher]) {
  class From[T <: Annotation : Manifest] {
    def from(method: Method): Option[T] = matchers.flatMap(_.get[T](method)).headOption
  }

  def get[T <: Annotation : Manifest]: From[T] = new From[T]
}
