import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val job1 = async {
        throw IllegalArgumentException()
    }

    delay(1_000L)
}
