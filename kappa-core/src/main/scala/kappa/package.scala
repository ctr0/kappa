
import java.io.StringReader
import java.nio.ByteBuffer
import java.util.Properties
import java.util.function.{Function => JavaFunction}

/**
  * Created by j0rd1 on 4/11/16.
  */
package object kappa {

  implicit def javaFuncToScalaFunc[T, R](f: JavaFunction[T, R]): T => R = {
    new Function[T, R] {
      override def apply(t: T): R = f.apply(t)
    }
  }

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
