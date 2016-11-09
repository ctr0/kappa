
import java.io.StringReader
import java.nio.ByteBuffer
import java.util.Properties

/**
  * Created by j0rd1 on 4/11/16.
  */
package object kappa {

  def Directive0(name: String)(f: KappaSession ⇒ Unit): Directive0 = new Directive0(name, f)

  def Directive1[T1](name: String)(f: KappaSession ⇒ T1): Directive1[T1] = new Directive1[T1](name, f)

  def Directive2[T1, T2](name: String)(f: KappaSession ⇒ (T1, T2)): Directive2[T1, T2] = new Directive2[T1, T2](name, f)


  implicit def path2String(path: Path): String = path.value


  implicit def string2Path(path: String): Path = Path(path)


  implicit class PathStringContext(val sc: StringContext) extends AnyVal {

    def p(args: Any*): Path = Path(args.map(_.toString): _*)
  }

  implicit class PathString(val string: String) extends AnyVal {

    def p(args: Any*): Path = Path(args.map(_.toString): _*)
  }


  type KafkaOffsets = Seq[PartitionOffset]


  case class PartitionOffset(partition: Int, offset: Long)


  object RawData {

    def apply(bytes: Array[Byte]) = new RawData(bytes)

    def apply(map: Map[String, String]) = ???
  }


  class RawData(val bytes: Array[Byte]) extends AnyVal {

    def asUtf8String = new String(bytes, "UTF-8")

    def asInt = ByteBuffer.wrap(bytes).getInt

    def asLong = ByteBuffer.wrap(bytes).getLong

    def asByteBuffer = ByteBuffer.wrap(bytes)

    def asMap: Map[String, String] = {
      import scala.collection.JavaConverters._
      val properties = new Properties()
      properties.load(new StringReader(asUtf8String))
      properties.asScala.toMap
    }

  }


}
