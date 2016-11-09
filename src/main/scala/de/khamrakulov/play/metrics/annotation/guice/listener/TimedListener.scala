package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.annotation.Timed
import de.khamrakulov.play.metrics.annotation.MetricNamer
import de.khamrakulov.play.metrics.annotation.guice.interceptor.TimedInterceptor
import de.khamrakulov.play.metrics.annotation.matcher.AnnotationProvider

/**
  * A listener which adds method interceptors to timed methods.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object TimedListener {
  def apply(registry: MetricRegistry, namer: MetricNamer, provider: AnnotationProvider) =
    new TimedListener(registry, namer, provider)
}

private[annotation] class TimedListener(registry: MetricRegistry, namer: MetricNamer, provider: AnnotationProvider)
  extends DeclaredMethodsTypeListener {

  protected def getInterceptor(method: Method) = provider.get[Timed].from(method).map { annotation =>
    TimedInterceptor(registry.timer(namer.timed(method, annotation)))
  }

}
