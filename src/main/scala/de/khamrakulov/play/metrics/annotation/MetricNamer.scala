package de.khamrakulov.play.metrics.annotation

import java.lang.reflect.Method
import javax.annotation.Nonnull

import com.codahale.metrics.MetricRegistry.{name => MetricName}
import com.codahale.metrics.annotation._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
trait MetricNamer {
  @Nonnull def counted(@Nonnull method: Method, @Nonnull counted: Counted): String

  @Nonnull def metered(@Nonnull method: Method, @Nonnull exceptionMetered: ExceptionMetered): String

  @Nonnull def gauge(@Nonnull method: Method, @Nonnull gauge: Gauge): String

  @Nonnull def metered(@Nonnull method: Method, @Nonnull metered: Metered): String

  @Nonnull def timed(@Nonnull method: Method, @Nonnull timed: Timed): String
}

/**
  * Implements the default metric naming policy: uses the name fields in the metric annotations.
  */
object DefaultMetricNamer {
  private[annotation] val COUNTER_SUFFIX: String = "counter"
  private[annotation] val COUNTER_SUFFIX_MONOTONIC: String = "current"
  private[annotation] val GAUGE_SUFFIX: String = "gauge"
  private[annotation] val METERED_SUFFIX: String = "meter"
  private[annotation] val TIMED_SUFFIX: String = "timer"
}

class DefaultMetricNamer extends MetricNamer {
  @Nonnull def counted(@Nonnull method: Method, @Nonnull counted: Counted): String = if (counted.absolute) {
    counted.name
  } else if (counted.name.isEmpty) {
    if (counted.monotonic) MetricName(method.getDeclaringClass, method.getName, DefaultMetricNamer.COUNTER_SUFFIX_MONOTONIC)
    else MetricName(method.getDeclaringClass, method.getName, DefaultMetricNamer.COUNTER_SUFFIX)
  } else {
    MetricName(method.getDeclaringClass, counted.name)
  }

  @Nonnull def metered(@Nonnull method: Method, @Nonnull exceptionMetered: ExceptionMetered): String = if (exceptionMetered.absolute) {
    exceptionMetered.name
  } else if (exceptionMetered.name.isEmpty) {
    MetricName(method.getDeclaringClass, method.getName, ExceptionMetered.DEFAULT_NAME_SUFFIX)
  } else {
    MetricName(method.getDeclaringClass, exceptionMetered.name)
  }

  @Nonnull def gauge(@Nonnull method: Method, @Nonnull gauge: Gauge): String = if (gauge.absolute) {
    gauge.name
  } else if (gauge.name.isEmpty) {
    MetricName(method.getDeclaringClass, method.getName, DefaultMetricNamer.GAUGE_SUFFIX)
  } else {
    MetricName(method.getDeclaringClass, gauge.name)
  }

  @Nonnull def metered(@Nonnull method: Method, @Nonnull metered: Metered): String = if (metered.absolute) {
    metered.name
  } else if (metered.name.isEmpty) {
    MetricName(method.getDeclaringClass, method.getName, DefaultMetricNamer.METERED_SUFFIX)
  } else {
    MetricName(method.getDeclaringClass, metered.name)
  }

  @Nonnull def timed(@Nonnull method: Method, @Nonnull timed: Timed): String = if (timed.absolute) {
    timed.name
  } else if (timed.name.isEmpty) {
    MetricName(method.getDeclaringClass, method.getName, DefaultMetricNamer.TIMED_SUFFIX)
  } else {
    MetricName(method.getDeclaringClass, timed.name)
  }
}
