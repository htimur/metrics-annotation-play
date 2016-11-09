package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.google.inject.TypeLiteral
import com.google.inject.matcher.Matchers
import com.google.inject.spi.{TypeEncounter, TypeListener}
import org.aopalliance.intercept.MethodInterceptor

/**
  * A TypeListener which delegates to `DeclaredMethodsTypeListener#getInterceptor(Method)` for each method in the
  * class's declared methods.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] trait DeclaredMethodsTypeListener extends TypeListener {

  def hear[T](literal: TypeLiteral[T], encounter: TypeEncounter[T]): Unit = {
    val klass = literal.getRawType
    for (method <- klass.getDeclaredMethods if !method.isSynthetic) {
      getInterceptor(method) match {
        case Some(interceptor) =>
          encounter.bindInterceptor(Matchers.only(method), interceptor)
        case _ =>
      }
    }
  }

  /**
    * Called for every method on every class in the type hierarchy of the visited type
    *
    * @param method method to get interceptor for
    * @return null if no interceptor should be applied, else an interceptor
    */
  protected def getInterceptor(method: Method): Option[MethodInterceptor]
}

