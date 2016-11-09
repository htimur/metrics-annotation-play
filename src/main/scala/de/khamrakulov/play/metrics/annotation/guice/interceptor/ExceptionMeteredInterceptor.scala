package de.khamrakulov.play.metrics.annotation.guice.interceptor

import com.codahale.metrics.Meter
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}

/**
  * A method interceptor which measures the rate at which the annotated method throws exceptions of a given type.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object ExceptionMeteredInterceptor {
  def apply(meter: Meter, klass: Class[_ <: Throwable]) = new ExceptionMeteredInterceptor(meter, klass)
}

class ExceptionMeteredInterceptor(meter: Meter, klass: Class[_ <: Throwable]) extends MethodInterceptor {
  override def invoke(invocation: MethodInvocation) =
    try invocation.proceed
    catch {
      case t: Throwable =>
        if (klass.isAssignableFrom(t.getClass)) meter.mark()
        throw t
    }
}
