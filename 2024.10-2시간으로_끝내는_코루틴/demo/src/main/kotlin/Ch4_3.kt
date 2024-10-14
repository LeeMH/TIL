import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job = launch {
        try {
            delay(1_000L)
        } catch(e: CancellationException) {
            // 아무것도 안한다.
        }

        printWithThread("delay에 의해 취소되지 않았다!!!")
    }

    delay(100)
    printWithThread("취소 시작")
    job.cancel()
}
