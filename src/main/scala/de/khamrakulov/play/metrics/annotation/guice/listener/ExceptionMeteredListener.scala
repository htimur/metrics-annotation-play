package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.annotation.ExceptionMetered
import de.khamrakulov.play.metrics.annotation.MetricNamer
import de.khamrakulov.play.metrics.annotation.guice.interceptor.ExceptionMeteredInterceptor
import de.khamrakulov.play.metrics.annotation.matcher.AnnotationProvider

/**
  * A listener which adds method interceptors to methods that should be instrumented for exceptions.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object ExceptionMeteredListener {
  def apply(metricRegistry: MetricRegistry, metricNamer: MetricNamer, provider: AnnotationProvider) =
    new ExceptionMeteredListener(metricRegistry, metricNamer, provider)
}

private[annotation] class ExceptionMeteredListener(metricRegistry: MetricRegistry,
                                              metricNamer: MetricNamer,
                                              provider: AnnotationProvider) extends DeclaredMethodsTypeListener {

  protected def getInterceptor(method: Method) = provider.get[ExceptionMetered].from(method).map { annotation =>
    val meter = metricRegistry.meter(metricNamer.metered(method, annotation))
    ExceptionMeteredInterceptor(meter, annotation.cause)
  }

}
