import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = CoroutineScope(Dispatchers.Default).launch {
        throw IllegalArgumentException()
    }

    delay(1_000L)
}
