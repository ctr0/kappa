package kappa

import org.apache.curator.utils.ZKPaths

object Path {

  def apply(basePath: String, anotherPath: String): Path = Path {
    ZKPaths.makePath("", basePath, anotherPath)
  }

  def apply(basePath: String, anotherPath: String, morePaths: String*): Path = Path {
    ZKPaths.makePath(basePath, anotherPath, morePaths: _*)
  }

  def apply(paths: String*): Path = Path {
    if (paths == null || paths.isEmpty) ""
    else ZKPaths.makePath("", "", paths: _*)
  }
}


case class Path(value: String) extends AnyVal {

  def /(path: String): Path = {
    Path(ZKPaths.makePath(value, path))
  }
/*
  def /(path: Path): Path = {
    Path(ZKPaths.makePath(value, path.value))
  }
*/
  override def toString: String = value

}