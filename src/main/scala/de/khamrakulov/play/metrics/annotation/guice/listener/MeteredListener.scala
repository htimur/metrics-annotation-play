package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.annotation.Metered
import de.khamrakulov.play.metrics.annotation.MetricNamer
import de.khamrakulov.play.metrics.annotation.guice.interceptor.MeteredInterceptor
import de.khamrakulov.play.metrics.annotation.matcher.AnnotationProvider

/**
  * A listener which adds method interceptors to metered methods.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object MeteredListener {
  def apply(registry: MetricRegistry, namer: MetricNamer, provider: AnnotationProvider) =
    new MeteredListener(registry, namer, provider)
}

private[annotation] class MeteredListener(registry: MetricRegistry, namer: MetricNamer, provider: AnnotationProvider)
  extends DeclaredMethodsTypeListener {

  protected def getInterceptor(method: Method) = provider.get[Metered].from(method).map { annotation =>
    MeteredInterceptor(registry.meter(namer.metered(method, annotation)))
  }
}
