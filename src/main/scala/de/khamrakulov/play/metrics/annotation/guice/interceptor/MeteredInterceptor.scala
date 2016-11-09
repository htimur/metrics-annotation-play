package de.khamrakulov.play.metrics.annotation.guice.interceptor

import com.codahale.metrics.Meter
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}

/**
  * A method interceptor which creates a meter for the declaring class with the given name (or the method's name, if none
  * was provided), and which measures the rate at which the annotated method is invoked.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object MeteredInterceptor {
  def apply(meter: Meter) = new MeteredInterceptor(meter)
}

private[annotation] class MeteredInterceptor(meter: Meter) extends MethodInterceptor {
  override def invoke(invocation: MethodInvocation): AnyRef = {
    meter.mark()
    invocation.proceed
  }
}
