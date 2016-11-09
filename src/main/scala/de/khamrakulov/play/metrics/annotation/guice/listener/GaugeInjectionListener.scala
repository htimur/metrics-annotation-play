package de.khamrakulov.play.metrics.annotation.guice.listener

import java.lang.reflect.Method

import com.codahale.metrics.{Gauge, MetricRegistry}
import com.google.inject.spi.InjectionListener

/**
  * An injection listener which creates a gauge for the declaring class with the given name (or the method's name, if
  * none was provided) which returns the value returned by the annotated method.
  *
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
private[annotation] object GaugeInjectionListener {
  def apply(metricRegistry: MetricRegistry, metricName: String, method: Method) =
    new GaugeInjectionListener(metricRegistry, metricName, method)
}

private[annotation] class GaugeInjectionListener[I](metricRegistry: MetricRegistry, metricName: String, method: Method) extends InjectionListener[I] {
  def afterInjection(i: I) = if (metricRegistry.getGauges.get(metricName) == null) {
    metricRegistry.register(metricName, new Gauge[AnyRef]() {
      def getValue = try method.invoke(i) catch {
        case e: Exception => new RuntimeException(e)
      }
    })
  }
}

