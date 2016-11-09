package de.khamrakulov.play.metrics.annotation.guice.interceptor

import java.util.concurrent.TimeUnit

import com.codahale.metrics.Timer
import org.aopalliance.intercept.{MethodInterceptor, MethodInvocation}

/**
  * A method interceptor which times the execution of the annotated method.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object TimedInterceptor {
  def apply(timer: Timer) = new TimedInterceptor(timer)
}

private[annotation] class TimedInterceptor(timer: Timer) extends MethodInterceptor {
  override def invoke(invocation: MethodInvocation): AnyRef = {
    // Since these timers are always created via the default ctor (via MetricRegister#timer), they always use
    // nanoTime, so we can save an allocation here by not using Context.
    val start: Long = System.nanoTime
    try
      invocation.proceed
    finally timer.update(System.nanoTime - start, TimeUnit.NANOSECONDS)
  }
}
