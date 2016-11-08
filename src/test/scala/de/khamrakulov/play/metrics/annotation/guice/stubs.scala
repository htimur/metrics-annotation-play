package de.khamrakulov.play.metrics.annotation.guice

import com.codahale.metrics.annotation.Counted

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
object stubs {
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
}
