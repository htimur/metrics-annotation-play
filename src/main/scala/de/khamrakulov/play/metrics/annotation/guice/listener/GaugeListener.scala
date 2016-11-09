package de.khamrakulov.play.metrics.annotation.guice.listener

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.annotation.Gauge
import com.google.inject.TypeLiteral
import com.google.inject.spi.{TypeEncounter, TypeListener}
import de.khamrakulov.play.metrics.annotation.MetricNamer
import de.khamrakulov.play.metrics.annotation.matcher.AnnotationProvider

/**
  * A listener which adds gauge injection listeners to classes with gauges.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object GaugeListener {
  def apply(metricRegistry: MetricRegistry, metricNamer: MetricNamer, provider: AnnotationProvider) =
    new GaugeListener(metricRegistry, metricNamer, provider)
}

private[annotation] class GaugeListener(metricRegistry: MetricRegistry, metricNamer: MetricNamer, provider: AnnotationProvider) extends TypeListener {
  def hear[T](literal: TypeLiteral[T], encounter: TypeEncounter[T]) = {
    val klass = literal.getRawType

    helper(klass, encounter)
  }

  private def helper[T, I >: T](klass: Class[I], encounter: TypeEncounter[T]): Unit = if (klass != null) {
    for (method <- klass.getDeclaredMethods if !method.isSynthetic) {
      provider.get[Gauge].from(method).foreach { annotation =>
        if (method.getParameterTypes.length == 0) {
          val metricName = metricNamer.gauge(method, annotation)
          if (!method.isAccessible) method.setAccessible(true)
          encounter.register(new GaugeInjectionListener[I](metricRegistry, metricName, method))
        }
        else encounter.addError("Method %s is annotated with @Gauge but requires parameters.", method)
      }
    }
    helper(klass.getSuperclass, encounter)
  }
}
