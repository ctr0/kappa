package kappa.spark.rest.v1

import java.io.ByteArrayOutputStream

/**
  * Created by j0rd1 on 2/12/16.
  */
class ClusterApiV1(host: String) extends RestApi(s"$host/v1") {

  def submit(submission: Submission): SubmissionResponse = {
    val out = new ByteArrayOutputStream()
    mapper.writeValue(out, submission)
    mapper.readValue[SubmissionResponse] {
      post("submissions/create", out.toString)
    }
  }

  def status(id: String): StatusResponse = {
    mapper.readValue[StatusResponse] {
      get(s"submissions/status/$id")
    }
  }

  def kill(id: String): KillResponse = {
    mapper.readValue[KillResponse] {
      get(s"submissions/kill/$id")
    }
  }

}
