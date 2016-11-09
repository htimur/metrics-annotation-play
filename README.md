[![Build Status](https://travis-ci.org/htimur/metrics-annotation-play.svg?branch=master)](https://travis-ci.org/htimur/metrics-annotation-play)

# Metrics Annotation Support for Play Framework
Metrics Annotations Support for Play Framework through Guice AOP. Inspired by [Dropwizard Metrics Guice](https://github.com/palominolabs/metrics-guice). 

## Dependencies

* [metrics-play](https://github.com/breadfan/metrics-play)

# Quick Start

### Get the artifacts

Artifacts are released in [Bintray](https://bintray.com/). For gradle, use the `jcenter()` repository. For maven, [go here](https://bintray.com/bintray/jcenter?filterByPkgName=com.palominolabs.metrics%3Ametrics-guice) and click "Set me up".

Maven:

### Install the module

```hocon
// in your application.conf file
play.modules.enabled += "de.khamrakulov.play.metrics.annotation.MetricsAnnotationModule"
```

### Use it

The `MetricsInstrumentationModule` you installed above will create and appropriately invoke a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) for `@Timed` methods, a [Meter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#meters) for `@Metered` methods, a [Counter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#counters) for `@Counted` methods, and a [Gauge](https://dropwizard.github.io/metrics/3.1.0/manual/core/#gauges) for `@Gauge` methods. `@ExceptionMetered` is also supported; this creates a `Meter` that measures how often a method throws exceptions.

The annotations have some configuration options available for metric name, etc. You can also provide a custom `MetricNamer` implementation if the default name scheme does not work for you.

#### Example

If you have a method like this:

```scala
class SuperCriticalFunctionality {
    def doSomethingImportant = {
        // critical business logic
    }
}
```

and you want to use a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) to measure duration, etc, you could always do it by hand:

```scala
def doSomethingImportant() = {
    // timer is some Timer instance
    val context = timer.time()
    try // critical business logic
    finally context.stop()
}
```

However, if you're instantiating that class with Guice, you could just do this:

```scala
@Timed
def doSomethingImportant = {
    // critical business logic
}
```

### Limitations

Since this uses Guice AOP, instances must be created by Guice; see [the Guice wiki](https://github.com/google/guice/wiki/AOP). This means that using a Provider where you create the instance won't work, or binding a singleton to an instance, etc.

Guice AOP doesn't allow us to intercept method calls to annotated methods in supertypes, so `@Counted`, etc, will not have metrics generated for them if they are in supertypes of the injectable class. One small consolation is that `@Gauge` methods can be anywhere in the type hierarchy since they work differently from the other metrics (the generated Gauge object invokes the `java.lang.reflect.Method` directly, so we can call the supertype method unambiguously).

# History

This module started from the state of metrics-guice immediately before it was removed from the [main metrics repo](https://github.com/dropwizard/metrics) in [dropwizard/metrics@e058f76dabf3f805d1c220950a4f42c2ec605ecd](https://github.com/dropwizard/metrics/commit/e058f76dabf3f805d1c220950a4f42c2ec605ecd).
