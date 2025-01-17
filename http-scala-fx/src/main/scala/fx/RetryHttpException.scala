package fx

import java.net.http.HttpResponse

opaque type RetryPolicyHttpException[A] =
  RetryPolicyHttpException.UnhandledRetryPolicyException |
    RetryPolicyHttpException.RetriesExhaustedException[A]

object RetryPolicyHttpException:
  private[fx] final case class UnhandledRetryPolicyException(
      message: String,
      cause: Throwable | Null)
      extends RuntimeException(message, cause)
  private[fx] final case class RetriesExhaustedException[A](r: HttpResponse[A])(
      using S: Show[HttpResponse[A]])
      extends RuntimeException:
    override def getMessage: String = s"${S.show(r)} retries exhausted."

  def unhandledException[A](ex: Throwable): RetryPolicyHttpException[A] =
    UnhandledRetryPolicyException(ex.getMessage, ex)
  def retriesExhausted[A](r: HttpResponse[A])(
      using Show[HttpResponse[A]]): RetryPolicyHttpException[A] = RetriesExhaustedException(r)
