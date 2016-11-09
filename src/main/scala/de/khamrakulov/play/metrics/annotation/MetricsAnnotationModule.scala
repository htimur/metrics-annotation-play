package de.khamrakulov.play.metrics.annotation

import com.google.inject.AbstractModule
import de.khamrakulov.play.metrics.annotation.guice.listener._
import play.api.{Configuration, Environment}

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class MetricsAnnotationModule(environment: Environment, configuration: Configuration) extends AbstractModule {
  override def configure(): Unit = {
    val config = MetricsAnnotationConfiguration(environment, configuration)

    bindListener(config.matcher, MeteredListener(config.registry, config.namer, config.annotationProvider))
    bindListener(config.matcher, TimedListener(config.registry, config.namer, config.annotationProvider))
    bindListener(config.matcher, GaugeListener(config.registry, config.namer, config.annotationProvider))
    bindListener(config.matcher, ExceptionMeteredListener(config.registry, config.namer, config.annotationProvider))
    bindListener(config.matcher, CountedListener(config.registry, config.namer, config.annotationProvider))
  }
}
