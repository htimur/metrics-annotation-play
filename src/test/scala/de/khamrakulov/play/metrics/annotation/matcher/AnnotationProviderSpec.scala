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

    /**
      * assertNotNull(annotationProvider.getAnnotation(classOf[Timed], publicMethod))
      * assertNull(annotationProvider.getAnnotation(classOf[Metered], publicMethod))
      * assertNull(annotationProvider.getAnnotation(classOf[Counted], publicMethod))
      * *
      * assertNotNull(annotationProvider.getAnnotation(classOf[Timed], protectedMethod))
      * assertNotNull(annotationProvider.getAnnotation(classOf[Metered], protectedMethod))
      * assertNull(annotationProvider.getAnnotation(classOf[Counted], protectedMethod))
      * *
      * assertNotNull(annotationProvider.getAnnotation(classOf[Timed], packagePrivateMethod))
      * assertNull(annotationProvider.getAnnotation(classOf[Metered], packagePrivateMethod))
      * assertNull(annotationProvider.getAnnotation(classOf[Counted], packagePrivateMethod))
      */

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
