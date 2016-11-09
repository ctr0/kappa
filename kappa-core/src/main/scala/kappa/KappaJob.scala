package kappa

/**
  * Created by j0rd1 on 5/11/16.
  */
trait KappaJob {

  def session: KappaSession

  def domain: Path

}
