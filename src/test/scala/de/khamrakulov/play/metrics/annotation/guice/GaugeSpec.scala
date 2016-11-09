package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.MetricRegistry._
import de.khamrakulov.play.metrics.annotation.DefaultMetricNamer
import de.khamrakulov.play.metrics.annotation.guice.fake.{ClassInstrumentedWithGauge, MethodInstrumentedWithGauge}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class GaugeSpec extends FlatSpec with Matchers with GuiceInjectorHelper {
  "Gauge annotation" should "create metrics for annotated class and methods" in {
    val cInstance = instanceOf[ClassInstrumentedWithGauge]
    val mInstance = instanceOf[MethodInstrumentedWithGauge]

    cInstance.doASomeThing
    mInstance.doAThing

    val metric1 = registry.getGauges.get(name(classOf[ClassInstrumentedWithGauge], "doASomeThing", DefaultMetricNamer.GAUGE_SUFFIX))
    val metric2 = registry.getGauges.get(name(classOf[MethodInstrumentedWithGauge], "things"))

    metric1 should not be null
    metric2 should not be null

    metric1.getValue should equal("gaugeParent")
    metric2.getValue should equal("poop")
  }

  it should "create metrics with default name" in {
    val instance = instanceOf[MethodInstrumentedWithGauge]

    instance.doAnotherThing

    val metric = registry.getGauges.get(name(classOf[MethodInstrumentedWithGauge], "doAnotherThing", DefaultMetricNamer.GAUGE_SUFFIX))

    metric should not be null
    metric.getValue should equal("anotherThing")
  }

  it should "create metrics with absolute name" in {
    val instance = instanceOf[MethodInstrumentedWithGauge]

    instance.doAThingWithAbsoluteName

    val metric = registry.getGauges.get(name("absoluteName"))

    metric should not be null
    metric.getValue should equal("anotherThingWithAbsoluteName")
  }

  it should "create metrics for superclass" in {
    val instance = instanceOf[MethodInstrumentedWithGauge]
    val metric = registry.getGauges.get(name("gaugeParent"))

    metric should not be null
    metric.getValue should equal("gaugeParent")
  }

  it should "create metrics for superclass private method" in {
    val instance = instanceOf[MethodInstrumentedWithGauge]
    val metric = registry.getGauges.get(name("gaugeParentPrivate"))
    metric should not be null
    metric.getValue should equal("gaugeParentPrivate")
  }

  it should "create private gauges" in {
    val instance = instanceOf[MethodInstrumentedWithGauge]
    val metric = registry.getGauges.get(name(classOf[MethodInstrumentedWithGauge], "gaugePrivate"))

    metric should not be null
    metric.getValue should equal("gaugePrivate")
  }

}
