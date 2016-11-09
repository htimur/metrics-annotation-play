package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.MetricRegistry._
import com.codahale.metrics.{Meter, Timer}
import com.codahale.metrics.annotation.ExceptionMetered._
import de.khamrakulov.play.metrics.annotation.DefaultMetricNamer
import de.khamrakulov.play.metrics.annotation.guice.fake._
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class ExceptionMeteredSpec extends FlatSpec with Matchers with GuiceInjectorHelper {
  "ExceptionMetered annotations" should "create metrics with public scope" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "exceptionCounter"))
    metric should not be null
    try {
      instance.explodeWithPublicScope(true)
      fail("Expected an exception to be thrown")
    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L

    instance.explodeWithPublicScope(false)

    metric.getCount shouldBe 1L
  }

  it should "create metrics for all methods of annotated class" in {
    val instance = instanceOf[ClassInstrumentedWithExceptionMetered]
    val metric1 = registry.getMeters.get(name(classOf[ClassInstrumentedWithExceptionMetered], "someMethod", DEFAULT_NAME_SUFFIX))
    val metric2 = registry.getMeters.get(name(classOf[ClassInstrumentedWithExceptionMetered], "anotherMethod", DEFAULT_NAME_SUFFIX))

    metric1 should not be null
    metric2 should not be null

    try {
      instance.someMethod
      fail("Expected an exception to be thrown")
    } catch {
      case ignored: RuntimeException =>
    }

    metric1.getCount shouldBe 1L
    metric2.getCount shouldBe 0L

    try {
      instance.anotherMethod
      fail("Expected an exception to be thrown")
    } catch {
      case ignored: RuntimeException =>
    }

    metric1.getCount shouldBe 1L
    metric2.getCount shouldBe 1L
  }

  it should "create names with default suffix, if name isn't specified" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "explodeForUnnamedMetric", DEFAULT_NAME_SUFFIX))
    metric should not be null

    try {
      instance.explodeForUnnamedMetric
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L
  }

  it should "create metrics with specified name" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "n"))

    metric should not be null
    metric.getCount shouldBe 0L

    try {
      instance.explodeForMetricWithName
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L
  }

  it should "create metrics with absolute name" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name("absoluteName"))

    metric should not be null
    metric.getCount shouldBe 0L

    try {
      instance.explodeForMetricWithAbsoluteName
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L
  }

  it should "create metrics with default scope" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "explodeWithDefaultScope", DEFAULT_NAME_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 0L

    try {
      instance.explodeWithDefaultScope
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L
  }

  it should "create metrics with protected scope" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "explodeWithProtectedScope", DEFAULT_NAME_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 0L

    try {
      instance.explodeWithProtectedScope
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: RuntimeException =>
    }

    metric.getCount shouldBe 1L

  }

  it should "create metrics with specific exception type and subtype and unknown type" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "failures"))

    metric should not be null
    metric.getCount shouldBe 0L

    try {
      instance.errorProneMethod(new MyException)
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: MyException =>
    }

    metric.getCount shouldBe 1L

    try {
      instance.errorProneMethod(new MySpecialisedException)
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: MySpecialisedException =>
    }

    metric.getCount shouldBe 2L

    try {
      instance.errorProneMethod(new MyOtherException)
      fail("Expected an exception to be thrown")

    } catch {
      case ignored: MyOtherException =>
    }

    metric.getCount shouldBe 2L
  }

  it should "create metrics for additional options" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]
    try {
      instance.causeAnOutOfBoundsException
    } catch {
      case ignored: ArrayIndexOutOfBoundsException =>
    }

    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "things"))
    metric should not be null
    metric.getCount shouldBe 1L

  }

  it should "create metrics for both timer and exception meter" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]

    val timedMetric = registry.getTimers.get(name(classOf[MethodInstrumentedWithExceptionMetered], "timedAndException", DefaultMetricNamer.TIMED_SUFFIX))
    val errorMetric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "timedAndException", DEFAULT_NAME_SUFFIX))

    timedMetric should not be null
    errorMetric should not be null

    timedMetric shouldBe a[Timer]
    errorMetric shouldBe a[Meter]

    timedMetric.getCount shouldBe 0L
    errorMetric.getCount shouldBe 0L

    instance.timedAndException(null)

    timedMetric.getCount shouldBe 1L
    errorMetric.getCount shouldBe 0L

    try {
      instance.timedAndException(new RuntimeException)
      fail("Should have thrown an exception")

    } catch {
      case ignored: Exception =>
    }

    timedMetric.getCount shouldBe 2L
    errorMetric.getCount shouldBe 1L
  }

  it should "create metrics for both meter and exception meter" in {
    val instance = instanceOf[MethodInstrumentedWithExceptionMetered]

    val meterMetric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "meteredAndException", DefaultMetricNamer.METERED_SUFFIX))
    val errorMetric = registry.getMeters.get(name(classOf[MethodInstrumentedWithExceptionMetered], "meteredAndException", DEFAULT_NAME_SUFFIX))

    meterMetric should not be null
    errorMetric should not be null

    meterMetric shouldBe a[Meter]
    errorMetric shouldBe a[Meter]

    meterMetric.getCount shouldBe 0L
    errorMetric.getCount shouldBe 0L

    instance.meteredAndException(null)

    meterMetric.getCount shouldBe 1L
    errorMetric.getCount shouldBe 0L

    try {
      instance.meteredAndException(new RuntimeException)
      fail("Should have thrown an exception")

    } catch {
      case ignored: Exception =>
    }

    meterMetric.getCount shouldBe 2L
    errorMetric.getCount shouldBe 1L
  }

}
