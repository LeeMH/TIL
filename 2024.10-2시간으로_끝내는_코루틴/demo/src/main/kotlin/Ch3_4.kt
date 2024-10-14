import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{

    val job1 = launch {
        delay(1_000L)
        printWithThread("job 1")
    }
    job1.join() // job1이 종료된 후 job2가 실행된다.

    val job2 = launch {
        delay(1_000L)
        printWithThread("job 1")
    }
}
