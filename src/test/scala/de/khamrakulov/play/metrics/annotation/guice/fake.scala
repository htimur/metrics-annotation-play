package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.annotation._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object fake {

  class GenericThing[T] {
    private[guice] def doThing(t: T) {
    }
  }

  class StringThing extends GenericThing[String] {
    @Counted(name = "stringThing", absolute = true, monotonic = true) override private[guice] def doThing(s: String) {
    }
  }

  class MyException extends RuntimeException

  class MySpecialisedException extends MyException

  class MyOtherException extends RuntimeException

  class MethodsInstrumentedWithCounter {
    @Counted(name = "things", monotonic = true) def doAThing: String = "poop"

    @Counted(monotonic = true) def doAnotherThing: String = "anotherThing"

    @Counted(monotonic = false) def doYetAnotherThing: String = "anotherThing"

    @Counted(name = "absoluteName", absolute = true, monotonic = true) def doAThingWithAbsoluteName: String = "anotherThingWithAbsoluteName"
  }

  @Counted(name = "things", monotonic = true)
  class ClassInstrumentedWithCounter {
    def doAThing: String = "poop"

    def doAnotherThing: String = "anotherThing"

    def doYetAnotherThing: String = "anotherThing"

    def doAThingWithAbsoluteName: String = "anotherThingWithAbsoluteName"
  }

  class MethodInstrumentedWithExceptionMetered {
    @ExceptionMetered(name = "exceptionCounter") private[guice] def explodeWithPublicScope(explode: Boolean): String = if (explode) throw new RuntimeException("Boom!")
    else "calm"

    @ExceptionMetered private[guice] def explodeForUnnamedMetric: String = throw new RuntimeException("Boom!")

    @ExceptionMetered(name = "n") private[guice] def explodeForMetricWithName: String = throw new RuntimeException("Boom!")

    @ExceptionMetered(name = "absoluteName", absolute = true) private[guice] def explodeForMetricWithAbsoluteName: String = throw new RuntimeException("Boom!")

    @ExceptionMetered def explodeWithDefaultScope: String = throw new RuntimeException("Boom!")

    @ExceptionMetered protected[guice] def explodeWithProtectedScope: String = throw new RuntimeException("Boom!")

    @ExceptionMetered(name = "failures", cause = classOf[MyException]) def errorProneMethod(e: RuntimeException) {
      throw e
    }

    @ExceptionMetered(name = "things", cause = classOf[ArrayIndexOutOfBoundsException]) def causeAnOutOfBoundsException: Any = {
      val arr: Array[AnyRef] = Array()
      arr(1)
    }

    @Timed
    @ExceptionMetered def timedAndException(e: RuntimeException) {
      if (e != null) throw e
    }

    @Metered
    @ExceptionMetered def meteredAndException(e: RuntimeException) {
      if (e != null) throw e
    }
  }

  @ExceptionMetered
  class ClassInstrumentedWithExceptionMetered {

    def someMethod = throw new RuntimeException("Boom!")

    protected[guice] def anotherMethod = throw new RuntimeException("Another Boom!")
  }

  @Metered
  class ClassInstrumentedWithMetered {
    def doAThing: String = "poop"

    private[guice] def doAThingWithDefaultScope: String = "defaultResult"
  }

  class MethodInstrumentedWithMetered {
    @Metered(name = "things") def doAThing: String = "poop"

    @Metered private[guice] def doAThingWithPackagePrivateScope: String = "defaultResult"

    @Metered protected[guice] def doAThingWithProtectedScope: String = "defaultProtected"

    @Metered(name = "n") def doAThingWithName: String = "withName"

    @Metered(name = "nameAbs", absolute = true) def doAThingWithAbsoluteName: String = "absoluteName"
  }

  @Timed(name = "things")
  class ClassInstrumentedWithTimed {
    def doAThing: String = {
      Thread.sleep(10)
      "poop"
    }
  }

  class MethodInstrumentedWithTimed {
    @Timed(name = "things")
    @throws[InterruptedException]
    def doAThing: String = {
      Thread.sleep(10)
      "poop"
    }

    @Timed private[guice] def doAThingWithPackagePrivateScope: String = "defaultResult"

    @Timed protected[guice] def doAThingWithProtectedScope: String = "defaultProtected"

    @Timed(name = "absoluteName", absolute = true) def doAThingWithAbsoluteName: String = "defaultProtected"
  }

  @Gauge
  class ClassInstrumentedWithGauge {
    def doASomeThing: String = "gaugeParent"
  }

  class MethodInstrumentedWithGaugeParent {
    @Gauge(name = "gaugeParent", absolute = true) def gaugeParent: String = "gaugeParent"

    @Gauge(name = "gaugeParentPrivate", absolute = true) private def gaugeParentPrivate: String = "gaugeParentPrivate"
  }

  class MethodInstrumentedWithGauge extends MethodInstrumentedWithGaugeParent {
    @Gauge(name = "things") def doAThing: String = "poop"

    @Gauge def doAnotherThing: String = "anotherThing"

    @Gauge(name = "absoluteName", absolute = true) def doAThingWithAbsoluteName: String = "anotherThingWithAbsoluteName"

    @Gauge(name = "gaugePrivate") private def gaugePrivate: String = "gaugePrivate"
  }

}
