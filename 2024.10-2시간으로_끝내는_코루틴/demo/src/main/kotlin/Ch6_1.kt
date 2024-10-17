import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = launch {
        delay(500L)
        printWithThread("job1")
    }

    val job2 = launch {
        delay(700L)
        throw IllegalArgumentException("코루틴 실패!!")
    }
}
