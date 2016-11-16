package de.khamrakulov.play.metrics.annotation

import com.codahale.metrics.SharedMetricRegistries
import com.google.inject.Guice
import play.api.{Configuration, Environment}

import scala.reflect.ClassTag

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
package object guice {

  trait GuiceInjectorHelper {
    val env = Environment.simple()
    val registryName = s"registry${scala.util.Random.nextInt()}"
    val config = Configuration.load(env, Map("metrics.registry" -> registryName))
    val injector = Guice.createInjector(new MetricsAnnotationModule(env, config))
    val registry = SharedMetricRegistries.getOrCreate(registryName)

    def instanceOf[T](implicit klass: ClassTag[T]) = injector.getInstance(klass.runtimeClass).asInstanceOf[T]
  }

}
