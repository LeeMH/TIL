# [3장. 소켓 프로그래밍](https://thebook.io/006884/0170/)

## [3.1. 블로킹 소켓](https://thebook.io/006884/0172/)

* 함수를 호출(주로 IO작업?)후 스레드가 응답을 대기하는것을 블로킹 이라고 한다.

* 블로킹된 스레드의 상태는 waitable 이고, CPU 사용은 하지 않는다.

## [3.2. 네트워크 연결 및 송신](https://thebook.io/006884/0173/)

* TCP/IP 프로토콜은 연결지향 프로토콜이다.

* 따라서, 하나의 end-point는 하나의 end-point(ip:port)만 통신이 가능하다.


```c
main()
{
    s = socket(TCP);                 // ➊
    s.bind(any_port);                // ➋
    s.connect("55.66.77.88:5959");   // ➌
    s.send("hello");                 // ➍
    s.close();                       // ➎
} 
```

* 위는 소켓 프로그램의 의사 코드이다.

    1) TCP 소켓생성

    2) OS는 소켓에 임의의 port를 바인딩 한다.

    3) 목적지로 연결을 시도한다. 이 함수는 블로킹 함수이다. 따라서, 연결이 이뤄지거나 에러가 발생할때까지 블로킹 된다.

    4) 소켓을 통해 데이터를 송신한다. 함수에서 리턴되었다고 상대방이 데이터를 수신한것은 아니다. 단지 애플리케이션에서 운영체제까지 데이터를 보낸것이다.


## [3.3. 블로킹과 소켓버퍼](https://thebook.io/006884/0175/)

* 소켓은 각각 송신버퍼와 수신버퍼를 가진다.

* 송신버퍼는 FIFO로서 먼저들어온 데이터를 우선 전송한다.

* 송신버퍼가 full 상태이면 send 함수를 호출한 스레드는 블로킹 된다.

* 이후 송신버퍼가 비워지면, send 함수의 메세지가 송신 버퍼로 복사되고 블로킹이 해제된다.

## [3.4. 네트워크 연결받기 및 수신](https://thebook.io/006884/0182/)

```c
main()
{
    s = socket(TCP);        // ➊
    s.bind(5959);           // ➋
    s.listen();             // ➌
    s2 = s.accept();        // ➍
    print(getpeeraddr(s2)); // ➎
    while (true)
    {
        r = s2.recv();      // ➏
        if (r.length <= 0)  // ➐
            break;
        print(r);
    }
    s2.close();             // ➑
}
```

* 위는 소켓 프로그램의 의사 코드이다.

    1) TCP 소켓생성

    2) 5959번을 바인딩 합니다. 만약 다른 프로세스에서 사용중이라면 실패.

    3) 이제 소켓은 듣는 역할을 수행 합니다.

    4) 연결이 들어올때까지 기다리고, 연결이 되면 새로운 소켓을 리턴합니다. 즉, 원래 소켓은 연결만 담당하고 각 연결에 대해서는 새로운 소켓이 담당하게 됩니다.

    5) 상대편 소켓 정보를 출력

    6) 상대편으로부터 메세지를 수신합니다. 수신할때까지 블로킹됩니다.

    7) 수신 길이가 0보다 작다면 연결이 끊겼음을 의미하므로 루프를 종료합니다.

    8) 소켓을 종료 합니다.

* 수신하는 소켓의 버퍼는 송신과 반대입니다.

    * os는 push를 하고, 애플리케이션에서 pop을 합니다.

    * 버퍼가 비어있으면 블로킹이 발생합니다.

## [3.5. 수신 버퍼가 가득차면 발생하는 현상](https://thebook.io/006884/0185/)

* TCP 소켓

    * 수신측의 애플리케이션보다 os에서 채우는 속도가 빠르다면 소켓이 full이 됩니다.

    * 그러면 송신측의 send 함수는 블로킹이 걸리게 됩니다.

    * 즉, 송수신의 양측중 느린속도에 맞추게 됩니다.

* UDP 소켓

    * 수신측의 버퍼가 full이 상태에서 패킷이 도착하면 그 패킷은 버려집니다.

    * 수신측의 버퍼가 full이더라도 송신측의 send 함수는 블로킹 되지 않습니다.

    * 특정특정 클라이언트에서 빠른 속도로 패킷을 전송한다면, 상대적으로 속도가 느린 클라이언틩 패킷은 누락될수 있습니다. 이를 혼잡현상이라고 합니다.

## [3.6. 논블럭 소켓](https://thebook.io/006884/0188/)

* 사용자가 많아지면 스레드 기반의 소켓 프로그래밍을 하게 됩니다.

* 하지만 아래와 같은 문제가 발생할수 있습니다.

    * 메모리 문제: 스레드당 1M 스택을 가진다고 가정하면, 1,000개의 클라이언트만 붙어도 1G 메모리가 필요 합니다.

    * 컨텍스트 스위칭 문제: 빈번한 블로킹으로 인해 컨텍스트 스위칭이 발생하면서 부하가 생기게 됩니다.

* 대부분의 OS는 이러한 문제로 인해, 논블럭 소켓을 제공 합니다.

* 아래는 논블럭 소켓의 의사코드 입니다.

    1) 송신즉시 응답이 리턴되고 이 리턴값을 확인해야 한다. 리턴값이 EWOULDBLOCK 이면 블로킹걸릴 상황이란 뜻으로 다시 전송을 해야 한다. 

    2) 사실상 무한루프처럼 돌면서 CPU high 현상을 발생시킴. 

```c
void NonBlockSocketOperation()
{
  s = socket(TCP);
  ...;
  s.connect(...);
  // 논블록 소켓으로 변경
  s.SetNonBlocking(true);
 
  while (true)
  {
      // ➊
      r = s.send(dest, data);
      if (r = = EWOULDBLOCK)
      {
          // 블로킹 걸릴 상황이었다. 송신을 안 했다.
          continue;
      }
 
      if (r = = OK)
      {
          // 보내기 성공에 대한 처리
      }
      else
      {
          // 보내기 실패에 대한 처리
      }
      // ➋
  }
}
```

* 아래는 개선된 버전의 예제이다.

    1) 소켓중 이벤트가 있다면 즉시 리턴되고, 아니라면 100 밀리세컨드까지 대기한다.

```c
List<Socket> sockets;
 
void NonBlockSocketOperation()
{
  while (true)
  {
      // 100밀리초까지 대기 ➊
      // 1개라도 I/O 처리를 할 수 있는 상태가 되면
      // 그 전에라도 리턴
      select(sockets, 100ms);
 
      foreach(s in sockets)
      {
          // 논블록 수신 ➋
          (result, data) = s.receive();
          if (data.length > 0)
          {
              print(data);
          }
          else if (result != EWOULDBLOCK)
          {
              // 소켓 오류 처리를 한다.
          }
      }
  }
}
```

## [3.7. Overlapped I/O 혹은 비동기 I/O](https://thebook.io/006884/0202/)

* overlapped I/O는 윈도우에서만 사용가능

* 지금까지 논블럭 소켓을 알아보았고, 장점은 아래와 같습니다.

    * 스레드 블로킹이 없으므로, 중간에 통제가 가능

    * 한개의 스레드로 여러개의 소켓을 처리 가능

* 하지만 단점도 있습니다.

    * 소켓 I/O 함수가 WOULD_BLOCK을 자주 리턴하면, 재시도 호출 낭비 발생

    * 소켓 I/O 함수를 호출할때 입력하는 데이터 블록에 대한 복사 연산이 시작됨

    * connect 함수는 재시도 하지 않지만, send나 receive는 재시도 해야 함. 함수의 일관성이 없음



## [3.8. epoll](https://thebook.io/006884/0213/)

* epoll은 리눅스와 안드로이드에서사용 가능

* 논블럭 소켓에서는 소켓 리스트를 돌면서 이벤트를 확인해야 함.

* 소켓수가 늘어나면 늘어날수록 부하가 생김

* epoll은 소켓이 사용가능 상태가 되면 사용자에게 알려주는 역할을 하게됨. 소켓 리스트를 루프도는 부하가 없음.

```c
epoll = new epoll();             // ➊
foreach(s in sockets)
{
  epoll.add(s, GetUserPtr(s)); // ➋
}
 
events = epoll.wait(100ms);      // ➌
 
foreach(event in events)         // ➍
{
  s = event.socket;            // ➎
  // 위 epoll.add에 들어갔던 값을 얻는다.
  userPtr = event.userPtr;
  // 수신? 송신?
  type = event.type;
  if (type = = ReceiveEvent)
  {
      (result, data) = s.recv();
      if (data.length > 0)
      {
          // 수신된 데이터를 처리한다.
          Process(userPtr, s, data);
      }
  }
}
```

* 위는 epoll 소켓 예제

    * 2번 부분에서 소켓과 사용자 데이터를 epoll에 등록 할수 있다.

    * 3번 부분에서 입력한 시간만큼 블로킹하여 이벤트를 수신할수 있다. 입력된 시간 전이라도 이벤트가 생기면 즉시 리턴된다.

* epoll 트리거 타입

    * 레벨 트리거 : I/O 가능 상태

    * 엣지 트리거 : I/O 불가능 -> 가능 상태

* 엣지 트리거를 사용해야 할것 같지만, 주의할 사항이 있습니다.

    * I/O 호출을 WOULD_BLOCK 이 발생할때까지 반복해서 실행

    * 소켓은 논블럭킹으로 설정되어야 함

* 아래는 엣지 트리거 사용 의사 코드입니다.

```c
foreach(event in events) // ➍
{
  s = event.socket;    // ➎
  // 위 epoll.add에 들어갔던 값을 얻는다.
  userPtr = event.userPtr;
  // 수신? 송신?
  type = event.type;
  if (type = = ReceiveEvent)
  {
      while (true)
      {
          (result, data) = s.recv();
          if (data.length > 0)
          {
              // 수신된 데이터를 처리한다.
              Process(userPtr, s, data);
          }
          if (result = = EWOULDBLOCK)
              break;
      }
  }
}
```

* connect와 accept도 이벤트로 발생. 

    * connect == send 이벤트

    * accept == receive 이벤트

* 아래는 accept 의사 코드임

```c
foreach(event in events) // ➍
{
  s = event.socket;    // ➎
  // 위 epoll.add에 들어갔던 값을 얻는다.
  userPtr = event.userPtr;
  // 수신? 송신?
  type = event.type;
  if (type = = ReceiveEvent)
  {
      if (s가 리스닝 소켓이면)
      {
          s2 = s.accept();
      }
      else
      {
          s.recv();
      }
  }
}
```

## [3.9. IOCP](https://thebook.io/006884/0219/)

*  윈도우에서만 사용가능 skip

