import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = launch {
        delay(1_000L)
        printWithThread("job 1")
    }

    val job2 = launch {
        delay(1_000L)
        printWithThread("job 1")
    }
}
