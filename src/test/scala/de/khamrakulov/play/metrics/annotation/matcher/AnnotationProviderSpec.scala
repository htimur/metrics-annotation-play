package de.khamrakulov.play.metrics.annotation.matcher

import com.codahale.metrics.annotation.{Metered, Timed}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class AnnotationProviderSpec extends FlatSpec with Matchers {
  "AnnotationProvider" should "provide annotations based on used matchers" in {
    val annotationProvider = new AnnotationProvider(List(ClassAnnotationMatcher(), MethodAnnotationMatcher()))
    val klass = classOf[MixedAnnotatedClass]
    val publicMethod = klass.getDeclaredMethod("publicMethod")
    val protectedMethod = klass.getDeclaredMethod("protectedMethod")
    val packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod")

    annotationProvider.get[Timed].from(publicMethod) should not be empty
    annotationProvider.get[Timed].from(protectedMethod) should not be empty
    annotationProvider.get[Timed].from(packagePrivateMethod) should not be empty
  }

  @Timed class MixedAnnotatedClass {
    def publicMethod() {
    }

    @Metered protected def protectedMethod() {
    }

    private[matcher] def packagePrivateMethod() {
    }
  }
}
