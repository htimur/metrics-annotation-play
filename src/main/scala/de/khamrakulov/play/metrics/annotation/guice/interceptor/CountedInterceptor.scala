package de.khamrakulov.play.metrics.annotation.guice.interceptor

import com.codahale.metrics.Counter
import com.codahale.metrics.annotation.Counted
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object CountedInterceptor {
  def apply(counter: Counter, annotation: Counted) = new CountedInterceptor(counter, annotation)
}

private[annotation] class CountedInterceptor private[annotation](counter: Counter, annotation: Counted) extends MethodInterceptor {
  private val decrementAfterMethod = !annotation.monotonic

  override def invoke(invocation: MethodInvocation): AnyRef = {
    counter.inc()
    try invocation.proceed
    finally if (decrementAfterMethod) counter.dec()
  }
}
