package de.khamrakulov.play.metrics

import com.codahale.metrics.SharedMetricRegistries
import com.google.inject.matcher.Matchers
import de.khamrakulov.play.metrics.annotation.matcher.{AnnotationMatcher, AnnotationProvider}
import play.api.{Configuration, Environment}

import scala.collection.JavaConversions._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
package object annotation {

  private[metrics] object MetricsAnnotationConfiguration {
    def apply(env: Environment, conf: Configuration) = new MetricsAnnotationConfiguration(env, conf)
  }

  private[metrics] class MetricsAnnotationConfiguration(env: Environment, conf: Configuration) {
    private val rootConfig: String = "metrics-annotation"
    val registryName = conf.getString("metrics.name").getOrElse("default")
    val registry = SharedMetricRegistries.getOrCreate(registryName)
    val matcher = Matchers.any()
    val namer = env.classLoader.loadClass(conf.getString(s"$rootConfig.metric-namer").get).newInstance().asInstanceOf[MetricNamer]
    val annotationMatchers: List[AnnotationMatcher] =
      conf.getStringList(s"$rootConfig.annotation-matchers")
        .get
        .map { matcher => env.classLoader.loadClass(matcher).newInstance().asInstanceOf[AnnotationMatcher] }
        .toList
    val annotationProvider = AnnotationProvider(annotationMatchers)
  }

}
