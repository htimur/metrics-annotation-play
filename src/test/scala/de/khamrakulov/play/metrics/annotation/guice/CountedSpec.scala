package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.MetricRegistry._
import com.codahale.metrics.SharedMetricRegistries
import com.google.inject.Guice
import de.khamrakulov.play.metrics.annotation.{DefaultMetricNamer, MetricsAnnotationModule}
import de.khamrakulov.play.metrics.annotation.guice.stubs.{ClassInstrumentedWithCounter, MethodsInstrumentedWithCounter}
import org.scalatest._
import play.api.{Configuration, Environment}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class CountedSpec extends FlatSpec with Matchers {
  private val env = Environment.simple()
  private val config = Configuration.load(env)
  val injector = Guice.createInjector(new MetricsAnnotationModule(env, config))

  val registry = SharedMetricRegistries.getOrCreate("default")

  "Counted annotations" should "create metrics for annotated methods" in {
    val instance = injector.getInstance(classOf[MethodsInstrumentedWithCounter])
    instance.doAThing
    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "things"))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for annotated class methods" in {
    val instance = injector.getInstance(classOf[ClassInstrumentedWithCounter])
    instance.doAThing
    val metric = registry.getCounters.get(name(classOf[ClassInstrumentedWithCounter], "things"))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics with default parameters" in {
    val instance = injector.getInstance(classOf[MethodsInstrumentedWithCounter])
    instance.doAnotherThing

    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "doAnotherThing", DefaultMetricNamer.COUNTER_SUFFIX_MONOTONIC))
    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for method with default name and monotonic false" in {
    val instance = injector.getInstance(classOf[MethodsInstrumentedWithCounter])
    instance.doYetAnotherThing

    val metric = registry.getCounters.get(name(classOf[MethodsInstrumentedWithCounter], "doYetAnotherThing", DefaultMetricNamer.COUNTER_SUFFIX))
    metric should not be null
    metric.getCount shouldBe 0L // if things are working well then this should still be zero...
  }

  it should "create metrics for counters with absolute name" in {
    val instance = injector.getInstance(classOf[MethodsInstrumentedWithCounter])
    instance.doAThingWithAbsoluteName

    val metric = registry.getCounters.get(name("absoluteName"))
    metric should not be null
    metric.getCount shouldBe 1L
  }
}
