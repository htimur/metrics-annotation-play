package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.MetricRegistry._
import de.khamrakulov.play.metrics.annotation.DefaultMetricNamer
import de.khamrakulov.play.metrics.annotation.guice.fake.{ClassInstrumentedWithCounter, MethodsInstrumentedWithCounter, StringThing}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class CountedSpec extends FlatSpec with Matchers with GuiceInjectorHelper {

  "Counted annotations" should "create metrics for annotated methods" in {
    val instance = instanceOf[MethodsInstrumentedWithCounter]
    instance.doAThing
    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "things"))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for annotated class methods" in {
    val instance = instanceOf[ClassInstrumentedWithCounter]
    instance.doAThing
    val metric = registry.getCounters.get(name(classOf[ClassInstrumentedWithCounter], "things"))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics with default parameters" in {
    val instance = instanceOf[MethodsInstrumentedWithCounter]
    instance.doAnotherThing

    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "doAnotherThing", DefaultMetricNamer.COUNTER_SUFFIX_MONOTONIC))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for method with default name and monotonic false" in {
    val instance = instanceOf[MethodsInstrumentedWithCounter]
    instance.doYetAnotherThing

    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "doYetAnotherThing", DefaultMetricNamer.COUNTER_SUFFIX))
    metric should not be null
    metric.getCount shouldBe 0L // if things are working well then this should still be zero...
  }

  it should "create metrics for counters with absolute name" in {
    val instance = instanceOf[MethodsInstrumentedWithCounter]
    instance.doAThingWithAbsoluteName

    val metric = registry.getCounters.get(name("absoluteName"))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "count invocations of generic subtype" in {
    val instance = injector.getInstance(classOf[StringThing])
    instance.doThing("foo")

    val metric = registry.getCounters.get(name("stringThing"))
    metric should not be null
    metric.getCount shouldBe 1L
  }
}
