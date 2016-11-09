package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.MetricRegistry._
import de.khamrakulov.play.metrics.annotation.DefaultMetricNamer
import de.khamrakulov.play.metrics.annotation.guice.fake.{ClassInstrumentedWithMetered, MethodInstrumentedWithMetered}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class MeteredSpec extends FlatSpec with Matchers with GuiceInjectorHelper {
  "Metered annotations" should "create metrics for class annotated methods" in {
    val methodAnnotatedInstance = instanceOf[MethodInstrumentedWithMetered]
    val classAnnotatedInstance = instanceOf[ClassInstrumentedWithMetered]

    methodAnnotatedInstance.doAThing
    classAnnotatedInstance.doAThing

    val metric1 = registry.getMeters.get(name(classOf[MethodInstrumentedWithMetered], "things"))
    val metric2 = registry.getMeters.get(name(classOf[ClassInstrumentedWithMetered], "doAThing", DefaultMetricNamer.METERED_SUFFIX))

    metric1 should not be null
    metric2 should not be null

    metric1.getCount shouldBe 1L
    metric2.getCount shouldBe 1L
  }

  it should "create metrics for methods with package private scope" in {
    val instance = instanceOf[MethodInstrumentedWithMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithMetered], "doAThingWithPackagePrivateScope", DefaultMetricNamer.METERED_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 0L

    instance.doAThingWithPackagePrivateScope

    metric.getCount shouldBe 1L
  }

  it should "create metrics for protected scope methods" in {
    val instance = instanceOf[MethodInstrumentedWithMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithMetered], "doAThingWithProtectedScope", DefaultMetricNamer.METERED_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 0L

    instance.doAThingWithProtectedScope

    metric.getCount shouldBe 1L
  }

  it should "create metrics with specified name" in {
    val instance = instanceOf[MethodInstrumentedWithMetered]
    val metric = registry.getMeters.get(name(classOf[MethodInstrumentedWithMetered], "n"))

    metric should not be null
    metric.getCount shouldBe 0L

    instance.doAThingWithName

    metric.getCount shouldBe 1L
  }

  it should "create metrics with specified absolute name" in {
    val instance = instanceOf[MethodInstrumentedWithMetered]
    val metric = registry.getMeters.get(name("nameAbs"))

    metric should not be null
    metric.getCount shouldBe 0L

    instance.doAThingWithAbsoluteName

    metric.getCount shouldBe 1L
  }
}
