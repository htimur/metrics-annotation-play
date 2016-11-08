package de.khamrakulov.play.metrics.annotation.matcher

import com.codahale.metrics.annotation.{Counted, Metered, Timed}
import org.scalatest._

/**
  * @author Timur Khamrakulov <timur.khamrakulov@gmail.com>.
  */
class AnnotationMatcherSpec extends FlatSpec with Matchers {
  "ClassAnnotationMatcher" should "match class level annotations for methods" in {
    val matcher = ClassAnnotationMatcher()
    val klass = classOf[TypeLevelAnnotatedClass]
    val publicMethod = klass.getDeclaredMethod("publicMethod")
    val protectedMethod = klass.getDeclaredMethod("protectedMethod")
    val packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod")

    matcher.get[Timed](publicMethod) should not be empty
    matcher.get[Metered](publicMethod) should not be empty
    matcher.get[Counted](publicMethod) should not be empty

    matcher.get[Timed](protectedMethod) should not be empty
    matcher.get[Metered](protectedMethod) should not be empty
    matcher.get[Counted](protectedMethod) should not be empty

    matcher.get[Timed](packagePrivateMethod) should not be empty
    matcher.get[Metered](packagePrivateMethod) should not be empty
    matcher.get[Counted](packagePrivateMethod) should not be empty
  }

  "MethodAnnotationMatcher" should "match method level annotations for methods" in {
    val matcher = MethodAnnotationMatcher()
    val klass = classOf[MethodAnnotatedClass]
    val publicMethod = klass.getDeclaredMethod("publicMethod")
    val protectedMethod = klass.getDeclaredMethod("protectedMethod")
    val packagePrivateMethod = klass.getDeclaredMethod("packagePrivateMethod")

    matcher.get[Timed](publicMethod) should not be empty
    matcher.get[Metered](publicMethod) shouldBe empty
    matcher.get[Counted](publicMethod) shouldBe empty

    matcher.get[Metered](protectedMethod) should not be empty
    matcher.get[Timed](protectedMethod) shouldBe empty
    matcher.get[Counted](protectedMethod) shouldBe empty

    matcher.get[Timed](packagePrivateMethod) shouldBe empty
    matcher.get[Metered](packagePrivateMethod) shouldBe empty
    matcher.get[Counted](packagePrivateMethod) should not be empty
  }

  @Timed
  @Metered
  @Counted
  class TypeLevelAnnotatedClass {
    def publicMethod() {
    }

    protected def protectedMethod() {
    }

    private[matcher] def packagePrivateMethod() {
    }
  }

  class MethodAnnotatedClass {
    @Timed def publicMethod() {
    }

    @Metered protected def protectedMethod() {
    }

    @Counted private[matcher] def packagePrivateMethod() {
    }
  }

}
