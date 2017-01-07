[![Build Status](https://travis-ci.org/htimur/metrics-annotation-play.svg?branch=master)](https://travis-ci.org/htimur/metrics-annotation-play)
[ ![Download](https://api.bintray.com/packages/htimur/maven/metrics-annotation-play/images/download.svg) ](https://bintray.com/htimur/maven/metrics-annotation-play/_latestVersion)

# Metrics Annotation Support for Play Framework
Metrics Annotations Support for Play Framework through Guice AOP. Inspired by [Dropwizard Metrics Guice](https://github.com/palominolabs/metrics-guice). 

## Dependencies

* [Play Framework](https://github.com/playframework/playframework)
* [metrics-reporter-play](https://github.com/htimur/metrics-reporter-play)

# Quick Start

### Get the artifacts

Artifacts are released in [Bintray](https://bintray.com/). For sbt, use `resolvers += Resolver.jcenterRepo`, for gradle, use the `jcenter()` repository. For maven, [go here](https://bintray.com/htimur/maven/metrics-annotaion-play) and click "Set me up".

SBT:

```scala
libraryDependencies += Seq(
  "de.khamrakulov" %% "metrics-annotation-play" % "1.0.2"
)
```

Maven:
```xml
<dependency>
  <groupId>de.khamrakulov</groupId>
  <artifactId>metrics-annotation-play_2.11</artifactId>
  <version>1.0.2</version>
  <type>pom</type>
</dependency>
```

Gradle:
```groovy
compile 'de.khamrakulov:metrics-annotation-play_2.11:1.0.2'
```

The module will be automatically enabled.

### Use it

The `MetricsAnnotationModule` you installed above will create and appropriately invoke a [Timer](https://dropwizard.github.io/metrics/3.1.0/manual/core/#timers) for `@Timed`, a [Meter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#meters) for `@Metered`, a [Counter](https://dropwizard.github.io/metrics/3.1.0/manual/core/#counters) for `@Counted`, and a [Gauge](https://dropwizard.github.io/metrics/3.1.0/manual/core/#gauges) for `@Gauge`. `@ExceptionMetered` is also supported; this creates a `Meter` that measures how often a method throws exceptions.

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
or this:

```scala
@Timed
class SuperCriticalFunctionality {
    def doSomethingImportant = {
        // critical business logic
    }
}
```

### Type level annotations

Type level annotation support is implemented for: `Timed`, `Metered`, `Counted` and `ExceptionMetered` annotations.

### Configuration

```hocon
metrics {
  annotation {
    metric-namer = "de.khamrakulov.play.metrics.annotation.DefaultMetricNamer" //Metric namer implementation
    annotation-matchers = [ //Annotation matchers, to derrive annotations from type
      "de.khamrakulov.play.metrics.annotation.matcher.ClassAnnotationMatcher",
      "de.khamrakulov.play.metrics.annotation.matcher.MethodAnnotationMatcher",
    ]
  }
}
```

### Limitations

Since this uses Guice AOP, instances must be created by Guice; see [the Guice wiki](https://github.com/google/guice/wiki/AOP). This means that using a Provider where you create the instance won't work, or binding a singleton to an instance, etc.

Guice AOP doesn't allow us to intercept method calls to annotated methods in supertypes, so `@Counted`, etc, will not have metrics generated for them if they are in supertypes of the injectable class. One small consolation is that `@Gauge` methods can be anywhere in the type hierarchy since they work differently from the other metrics (the generated Gauge object invokes the `java.lang.reflect.Method` directly, so we can call the supertype method unambiguously).
