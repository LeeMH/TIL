import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        printWithThread("START")
        launch {
            delay(2_000L)
            printWithThread("LAUNCH END")
        }
    }
    // runBlocking 은 내부의 코루틴이 모두 완료될때 까지 스레드를 블로킹 한다.

    printWithThread("END")
}
