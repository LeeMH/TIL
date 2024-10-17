import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = async(SupervisorJob()) {
        throw IllegalArgumentException()
    }

    delay(1_000L)
    job1.await()
}
