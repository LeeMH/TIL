import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{

    val job = launch(start = CoroutineStart.LAZY) {
        printWithThread("hello!!")
    }

    printWithThread("START")
    delay(1_000)
    job.start()

    printWithThread("END")
}
