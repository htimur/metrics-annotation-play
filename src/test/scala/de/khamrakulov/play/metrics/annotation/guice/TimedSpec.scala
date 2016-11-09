package de.khamrakulov.play.metrics.annotation.guice

import java.util.concurrent.TimeUnit._

import com.codahale.metrics.MetricRegistry._
import de.khamrakulov.play.metrics.annotation.DefaultMetricNamer
import de.khamrakulov.play.metrics.annotation.guice.fake.{ClassInstrumentedWithTimed, MethodInstrumentedWithTimed}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class TimedSpec extends FlatSpec with Matchers with GuiceInjectorHelper {
  "Timed annotation" should "create metrics for annotated methods" in {
    val instance = instanceOf[MethodInstrumentedWithTimed]

    instance.doAThing

    val metric = registry.getTimers.get(name(classOf[MethodInstrumentedWithTimed], "things"))

    metric should not be null
    metric.getCount shouldBe 1L

    metric.getSnapshot.getMax should be > NANOSECONDS.convert(5, MILLISECONDS)
    metric.getSnapshot.getMax should be < NANOSECONDS.convert(20, MILLISECONDS)
  }

  it should "create metrics for annotated class" in {
    val instance = instanceOf[ClassInstrumentedWithTimed]

    instance.doAThing

    val metric = registry.getTimers.get(name(classOf[ClassInstrumentedWithTimed], "things"))

    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for package private scope" in {
    val instance = instanceOf[MethodInstrumentedWithTimed]

    instance.doAThingWithPackagePrivateScope

    val metric = registry.getTimers.get(name(classOf[MethodInstrumentedWithTimed], "doAThingWithPackagePrivateScope", DefaultMetricNamer.TIMED_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics for package protected scope" in {
    val instance = instanceOf[MethodInstrumentedWithTimed]

    instance.doAThingWithProtectedScope

    val metric = registry.getTimers.get(name(classOf[MethodInstrumentedWithTimed], "doAThingWithProtectedScope", DefaultMetricNamer.TIMED_SUFFIX))

    metric should not be null
    metric.getCount shouldBe 1L
  }

  it should "create metrics absolute name" in {
    val instance = instanceOf[MethodInstrumentedWithTimed]

    instance.doAThingWithAbsoluteName

    val metric = registry.getTimers.get(name("absoluteName"))

    metric should not be null
    metric.getCount shouldBe 1L
  }
}
