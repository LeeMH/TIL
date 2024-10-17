import kotlinx.coroutines.*

fun main(): Unit = runBlocking{
    val exceptionHandler = CoroutineExceptionHandler { _, throwable  ->
        // 사앹 저장등 작업을 수행한다.
        printWithThread("예외!!")

        // 만약 예외를 던지고 싶으면, 이부분에서  처리한다.
        //throw throwable
    }

    val job1 = CoroutineScope(Dispatchers.Default).launch(exceptionHandler) {
            throw IllegalArgumentException()
    }

    delay(1_000L)
}
