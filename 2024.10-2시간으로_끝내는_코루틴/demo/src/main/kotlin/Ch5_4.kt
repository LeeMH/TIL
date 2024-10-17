import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = CoroutineScope(Dispatchers.Default).async {
        throw IllegalArgumentException()
    }

    delay(1_000L)
    job1.await()
}
