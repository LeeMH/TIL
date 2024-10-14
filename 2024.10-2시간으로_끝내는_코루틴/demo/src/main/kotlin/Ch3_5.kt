import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{
    printWithThread("START")
    val job = async {
        delay(1_000L)
        10 + 2
    }

    printWithThread("result => ${job.await()}")
}
