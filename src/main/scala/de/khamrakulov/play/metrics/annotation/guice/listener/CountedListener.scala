package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.annotation.Counted
import de.khamrakulov.play.metrics.annotation.MetricNamer
import de.khamrakulov.play.metrics.annotation.guice.interceptor.CountedInterceptor
import de.khamrakulov.play.metrics.annotation.matcher.AnnotationProvider

/**
  * A listener which adds method interceptors to counted methods.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object CountedListener {
  def apply(metricRegistry: MetricRegistry, metricNamer: MetricNamer, provider: AnnotationProvider) =
    new CountedListener(metricRegistry, metricNamer, provider)
}

private[annotation] class CountedListener(metricRegistry: MetricRegistry,
                                     metricNamer: MetricNamer,
                                     provider: AnnotationProvider) extends DeclaredMethodsTypeListener {

  protected def getInterceptor(method: Method) = {
    provider.get[Counted].from(method).map { annotation =>
      val counter = metricRegistry.counter(metricNamer.counted(method, annotation))
      CountedInterceptor(counter, annotation)
    }
  }

}
