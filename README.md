# Mudle
두유노 디스 뮤직? 노래 다 가져와~ (2023.07.22 ~ 2023.08.08, 단독 프로젝트)<br/>
관련 - https://github.com/choi-hyeseong/MudleServer

![GIF 2023-08-08 오전 10-32-15](https://github.com/choi-hyeseong/Auction-Finder/assets/114974288/add57dfe-e182-4048-a34e-b28ed60012ee)

##### 사용된 기술 스택 : 
<img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white"> <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white"> 
##### 그외 :
kotlin Coroutines, hilt, STOMP (via websocket), retrofit (rest), MVVM

### 1. 제작 계기 
얼마전에 유튜브에서는 '스타크래프트'의 맵 제작 기능을 이용하여 여러가지 노래의 일부분을 들려주고 맞출 수 있게 만든 맵이 상당히 인기가 많았습니다.
<br/>하지만 이 '맵'의 특성상 맵 에셋 자체에 첨부된 노래만 재생이 가능하여 제한된 노래만 재생이 가능하다는 단점이 있었습니다. 
<br/>그래서 한번 이걸 어플로 만들어서 마음대로 노래를 신청하고 맞출 수 있게 하면 재밌지 않을까? 라는 생각이 들어 제작을 하게 되었습니다.
<br/>그리고, 잘 알려지지 않았지만 의외로 좋은 노래를 자유롭게 신청해서 다른 사람들과 나눌 수 있는 라디오의 느낌 또한 살려보고 싶었습니다.
<br/>제목은 기존에 단어 맞추기 게임인 'Wordle'에서 Music을 곁들여 제작하였습니다.

### 2. 구성 요소
회원가입 화면

![화면 캡처 2023-08-08 105032](https://github.com/choi-hyeseong/Mudle/assets/114974288/5a2953cc-6a30-469f-aba1-d2c7f8e67164)


노래 신청 다이얼로그

![화면 캡처 2023-08-08 105119](https://github.com/choi-hyeseong/Mudle/assets/114974288/5f30266d-7464-4dd6-bd5e-6adf017223ba)


### 3. 작동 방식
기본적으로 회원가입시 일정량의 '코인'을 지급 받고, 이 코인으로 음악재생을 요청할 수 있습니다.
</br>신청된 음악은 큐에 저장되어 있다 일정 주기로 하나씩 재생이 되며, 이때 채팅으로 음악의 제목을 맞추면 코인을 흭득 할 수 있습니다.
</br>음악 신청은 유튜브 id를 전송하면 서버에서 제목을 파싱 후 저장합니다. (url에서 watch?v={id})

### 3-1. Detail
![화면 캡처 2023-08-08 105053](https://github.com/choi-hyeseong/Mudle/assets/114974288/8092664a-ab13-4d05-8d42-7fb711d883ac)

처음, 회원가입시 VM에서 입력값을 검증한뒤 통과하지 못했을경우 LiveData에 사유를 작성합니다. 그리고 검증을 통과했을시 서버에 register POST요청을 넣어 회원가입을 진행합니다.
이후 STOMP 프로토콜이 연결되어 RecyclerView에 채팅값을 추가하고 보여줍니다. (이는 ListLiveData 구현체를 통해 관리됩니다.)
서버는 정기적으로 STOMP를 통해 스케줄링된 음악이 있다고 클라이언트에게 REQUEST요청을 보냅니다.
이 REQUEST요청을 읽으면 클라이언트는 현재 재생되는 음악의 정보를 얻을 수 있는 GET주소로 요청을 보내 음악의 정보를 흭득하고 재생합니다.
이후 노래의 정답이 맞춰지지 않았을때는 서버로 전송되는 채팅을 읽은 후, 노래의 제목을 포함할경우 정답으로 인식하고 포인트를 제공합니다.
이때 정답을 맞춘 UUID를 UPDATE요청과 함께 STOMP로 전송해 클라이언트 자신의 UUID와 일치할경우 유저의 정보를 가져올 수 있는 GET주소로 요청 후 정보를 업데이트 합니다.
노래 신청시 서버에 POST요청을 보내게 되고, 서버는 유튜브 주소에서 제목을 파싱해 큐에 저장하게 됩니다. 이때 오류가 발생하면 response를 통해 오류를 보여주고 이는 LiveData를 통해 Toast로 보여집니다.

### 4. 추가 할 수 있는 점
코인의 사용처 추가 (룰렛, 보상), 맞춘 횟수를 기준으로 한 랭킹, 사용자 아이콘 등등을 좀더 추가하고, UI적 개선을 하면 좋을 것 같습니다.
<br/> 만약 시간이 된다면 이 부분을 좀더 개선해서 실제 사용가능한 서비스로 구축해도 되지 않을까.. 라는 생각이 드네요

### 5. MVVM
기존에 MVC, MVP의 방식은 모델과 뷰의 결합도가 높아서 상당히 곤란했습니다. 액티비티의 경우 뷰와 컨트롤러의 역할을 동시에 하면서 모델을 가져오기도 했었습니다.<br/>
따라서 모델과 뷰의 결합도를 낮추기 위해 중간에 뷰모델을 넣어 이를 통해 상호작용 할 수 있는 MVVM (Model - View - ViewModel) 패턴을 적용하게 되었습니다. <br/>
처음에는 기존 MVC에 적응되어 있어서 새로운 패턴이 낯설었지만, MVC를 하면서 느낀 '액티비티에 많은 기능이 치중되어 있다'라는 점이 해결되어 좋았습니다.<br/>
또한 데이터바인딩을 통해 findViewById 메소드를 지양하게 되어 nullable하지 않게 view에 접근 할 수 있다는 점이 마음에 들었습니다.<br/>
하지만 LiveData를 통해 뷰와 뷰모델이 상호작용하여 데이터를 주고 받는데, 이 부분 하나하나를 observe하는 방식, 이 기능을 뷰모델에 구현해야 하는지, 뷰모델은 데이터를 바로 리턴하면 안되는지. 이러한 부분이 처음에는 좀 어려웠습니다. <br/>
하지만 적응되고 나니 상당히 깔끔한 코드를 작성할 수 있고, 코드를 수정할때에도 기존에 액티비티를 통째로 수정하던것에서 벗어나 유즈케이스와 같은 최소의 범위만 수정하면 된다는 점이 상당히 좋았습니다.<br/>
위 MVVM 패턴을 적용하면서 클린 아키텍처 또한 적용하고 싶어 뷰모델에서 유즈케이스에게 구현을 요청하고, 유즈케이스는 모델에 접근 하는 방식을 시도했었으나.. 이미 Service의 형태로 구현을 해버려서 유즈케이스의 구현을 못한 부분이 아쉬웠습니다.
서비스 또한 유즈케이스로 분리하면 하나하나 문제가 되는 부분만 수정할 수 있어 좋았을 것 같습니다.

따라서 이 프로젝트를 통해 좀더 MVVM이라는 패턴에 대해 친숙해지고, OOP적 시야를 늘릴 수 있는 좋은 기회가 된 것 같습니다.

### 6. 그외 라이브러리

##### 6-1. hilt
5번과 마찬가지로 hilt는 스프링처럼 DI를 쉽게 할 수 있는 라이브러리입니다. 기존에 MVVM에서 레포지토리 패턴을 적용한 뒤, 이를 VM에서 사용할 수 있게 해야하는데, 이를 어떻게 해야할지 고민했었습니다.
보통 테스트의 용이성을 토대로 생성자 DI의 형태로 코드를 구성했었는데, VM의 경우 안드로이드 Lifecycle의 관리를 위해 직접 ViewModel()의 형태로 생성하는게 아닌, ViewModelProvider를 통해 생성을 해야합니다.
하지만, 생성자에 파라미터가 있을경우 ViewModelProvider에서 생성하기 까다로웠습니다. 해당 VM을 제공하는 팩토리를 만들고.. 이를 제공하게 되면 VM하나당 팩토리를 1개씩 만들어야 해서 상당히 복잡했습니다.
이때 hilt를 적용하게 되면서 자동으로 위 과정을 수행해주니 어노테이션만 선언하면 깔끔하게 잘 작동되고, Retrofit과 같은 외부 객체도 싱글톤을 보장하며 사용할 수 있게되어 좋았습니다.

##### 6-2. 코루틴
안드로이드 UI 쓰레드에서는 네트워크 통신이 막혀있고, 해서도 안됩니다. 이 부분을 해결하기 위해 예전부터 쓰레드를 하나 생성하고, 이 쓰레드를 실행시키는 방법으로 구현해 왔었는데, 코루틴이라는 개념을 알게되어 적용했습니다.
코루틴은 파이썬과 같은 언어처럼 Parallel한 방식으로 코드를 작동하는 라이브러리입니다. 처음에는 이해하기 어려운 개념이였으나, 자동으로 관리해주는 Light-weight한 Thread라고 생각하니 이해하기 쉬웠습니다.
또한 C#과 같이 Async, Await의 기능과 기존 Thread의 join, Parallel한 작업을 동기적으로 접근하는것 과 같은 경험을 줄 수 있는 suspend 함수또한 마음에 들었습니다.
다만, suspend 함수를 다루는 부분이 조금 까다로웠던 부분이 조금 있었던것 같습니다.. suspend함수를 레포지토리에서 선언하고 이를 VM에서 코루틴 스코프를 열어 접근해도 되는지, 이런 부분이 좀 어려웠던것 같습니다.

##### 6-3. STOMP
기존에 다른 프로젝트에서 사용했던 WebSocket을 Text-Based로 구현한 STOMP라는 프로토콜?을 사용했습니다. 기존 WebSocket으로 구현할때는 스프링에서 소켓 세션을 일일히 인증하고, 이를 open해주고 하는 과정이 상당히 귀찮고 어려웠으나,
STOMP를 적용하면서 publish, subscribe방식으로 엔드포인트만 지정해주면 알아서 메시지를 래핑한다음 읽고 - 쓰는 방식이 너무 좋았습니다. 진작에 이걸 쓸걸 이라는 생각을 수십번들게 하네요.
STOMP WebSocket은 OKHttp내 WebSocket 쓰레드를 이용해 독립적으로 돌아가서 따로 쓰레드 / 코루틴을 생성해서 코드를 실행 할 필요가 없다는 점을 알게되어 기존 Thread부분을 제거하게 되었습니다.

##### 6-4. Retrofit
기존에는 OkHttp를 이용해 직접 Body를 파싱하고 읽어오는 수작업을 했었으나, Retrofit을 적용하니 인터페이스만 만들어두면 알아서 파싱해주고 Response를 리턴해주니 좋았습니다.
또한  기존에 Future<T>방식으로 비동기의 순서를 제어 했으나, suspend 함수 또한 제공하여 편하게 순서를 제어할 수 있어서 좋았습니다.
다만, suspend 함수를 사용한다 해서, REST요청을 넣고, 응답을 받아오는 과정까지는 Retrofit이 사용하는 OkHttp 구현체를 사용하고 있어 suspend함수 내에서 이를 핸들링할 필요는 없다는 점을 너무 늦게 깨달았던거 같습니다..
suspend 방식으로 구현을 할때는 기존 Call대신 Response등 동기적으로 처리하는 리턴값을 사용해 처리해야 했었으나, Response<T>의 경우 서버가 꺼져있거나 하는 등 오류가 발생시 어플이 크래시되는 문제가 있었습니다.
그래서 'SandWich'라는 라이브러리의 ApiRespose<T>를 이용해 좀더 클린한 코드로 이를 핸들링 할 수 있어 좋았습니다.

### 7. 시행착오

##### 7-1. currentTimeMillis 차이로 인한 클라이언트간 노래 시작 위치 차이
처음에는 노래가 스케줄링 될때 STOMP를 통해 노래 정보를 담아 전송했었습니다. 이때 해당 객체의 startTime을 서버의 currentTimeMillis를 사용했었는데, 가상기기에서는 잘 작동되었지만, 실 기기에서는 이 차이가 20초 이상 나서
노래의 시작시간이 서로 다른 문제가 있었습니다. 이는 게임을 진행했을때 상대적으로 유리한 부분이라 수정이 필요했습니다.
이 부분을 찾아보니 기기마다 설정된 시간이 다를경우 위 메소드가 리턴하는 값이 다를 수 있어 어떻게 수정할 지 생각하던중 '서버의 시간을 강제' 하기로 했습니다. 서버의 시작시간과 GET요청을 넣은 시간의 차이가 재생시간이니 이를
사용하도록 하니 기기가 달라도 서로 재생시간이 유사하게 흘러감을 알 수 있었습니다.
이는 노래가 재생중일때 어플을 실행시켜 노래 정보를 받아와야 하는 기기도 정상적으로 작동하는것을 알 수 있었습니다.

##### 7-1-1. 기기 회전
기기가 회전하고 홈버튼을 눌렀다가 다시 돌아올때와 같은 여러가지 상황에서는 어플이 '꺼지지'는 않지만, 내부적인 상태가 순식간에 변합니다. 이는 Android Lifecycle이라 하는데, 이 부분을 간과하고 구현했더니 기기를 회전시키거나
홈버튼을 눌렀다 다시 들어오니 재생중인 음악이 종료되는 등 문제가 있었습니다. 이를 해결하기 위해 onResume() 을 이용하여 음악을 다시 서버에서 받아오는 방식을 사용하여 LiveData에 셋팅해주니 잘 작동했었습니다.

##### 7-2. ListLiveData
RecyclerView는 동적으로 리스트를 추가할 수 있는 뷰 입니다. 이때 Adapter에 리스트를 넣어줘서 채팅을 갱신할 수 있는데, LiveData에 어떻게 리스트가 갱신되었는지 notify를 해줄 수 있는지 잘 몰랐습니다.
이를 찾아보니 내부적으로 liveData를 2개를 사용하여 이를 셋팅하고 불러오는 방식을 사용하는데, 이를 이용해 리스트방식의 MutableLiveData를 구현하여 잘 사용하게 되었습니다.

##### 7-3. LiveData
LiveData의 value를 설정해 데이터가 들어왔다는 notify를 줄 수 있는데, 갑자기 어플이 종료되었습니다. 사유는 백그라운드 쓰레드에서는 setValue를 쓰지 말라는거였습니다. 따라서 postValue를 이용하여 해결했습니다.

##### 7-4. DI
hilt적용 이전 생성자 DI를 이용하여 VM을 구성했었습니다. 이때 레포지토리가 리턴하는 LiveData가 observe되지 않아 한참을 보고 있었는데, DI 되는 객체에서 레포지토리를 추가적으로 생성하여 이를 사용하고 있었습니다.
따라서 서로 다른 LiveData를 리턴하니 작동되지 않았던것 이였습니다. 이후 이 문제를 해결한뒤 hilt를 적용한 후 레포지토리는 하나만 생성되야 한다는 싱글톤을 적용하여 위와 같은 문제가 발생되지 않게 했습니다.
싱글톤이 중요하다는 점을 다시한번 깨닫게 되었습니다.

##### 7-5. hilt
hilt는 매우 편리하고 좋지만 설정하기 까다로웠습니다. Module을 Provide할때 InstallIn에서 해당 모듈의 스코프 / lifecycle을 지정할 수 있는데, 서로 다른 모듈에서 DI의 참조가 발생할때 lifecycle이 다를경우 이를 인식하지 못해
오류가 발생했었습니다. 스코프를 동일하게 맞춰주고 난 후, REST에 필요한 Okhttp와 STOMP에 필요한 OkHttp 2개가 필요하여 어노테이션 기반으로 서로 구분 할 수 있게 Qualifier를 지정해줬는데 이를 또 인식하지 못했습니다.
이는 어노테이션 선언을 Module내에 해서 hilt가 인식 할 수 있게 해야했습니다. 수정하니 잘 돌아갔습니다.

##### 7-6. OkHttp
서버가 꺼져있을경우 TimeOut이 발생하면 어플이 강제종료 되었습니다. 이를 위해 SandWich 라이브러리를 적용하여 해결하였습니다. (STOMP는 timeout발생시 reconnect를 자동으로 실행해 에러 핸들링이 필요 없었습니다.)

##### 7-7. Kotlin Data Class with ObjectMapper
코틀린의 data class은 ObjectMapper와 호환되지 않아 이를 생성할 수 없었습니다. 따라서 생성자에 @JsonConstructor 어노테이션을 넣어주고, 필드에 Property를 지정해주니 잘 작동했습니다.

### 8. 아쉬운점

##### 8-1. 디자인을 좀더 이쁘게 하면 좋지 않았을까..
##### 8-2. 위에 말했던 대로 기능이 좀더 추가되면 좋았을 것 같은데
##### 8-3. 현재 *Service로 구현된 클래스를 UseCase로 잘게 쪼개 변화에 대응하면 좀더 유지보수 하기 좋을텐데
##### 8-4. DI를 너무 늦게 적용해 테스트 관련 기능을 많이 추가하지 못했습니다.. DI가 없을때는 필드에서 생성하는 방식이라 테스트에 대응할 수 없어 이에 소극적이였던 부분이 있었던것 같습니다.
##### 8-5. 백그라운드 gif
백그라운드에서 gif이미지로 이퀄라이저를 표시했는데, 원래는 이퀄라이저 라이브러리를 이용하여 노래에 맞춰 움직일 수 있게 할려고 했습니다..
근데 안드로이드 최신버젼에서 이퀄라이저가 오디오를 읽을 수 있는 시스템의 미디어 session id를 제공하지 않아 사용할 수 없었습니다.
또한 클라이언트에서 음악을 재생하는 방식은 유튜브 url을 플레이어에 입력하는 방식인데, 이 또한 WebView구현체를 기반으로 하여 세션id를 가져올 수 없었습니다..ㅜㅜ
##### 8-6. 노래 재생
이러한 게임은 순발력이 중요합니다. 0.1초의 차이로도 정답을 입력할 수 있기 때문입니다. 그래서 노래의 재생시간을 동기화 시키고 최대한 차이가 없도록 유지하고자 했지만 한계가 있었습니다.
따라서 개선할 수 있다면 현재 플레이어인 YoutubePlayer를 제거하고, 서버에서 mp3파일을 제공한뒤 STOMP를 통해 클라이언트의 노래 준비 여부를 확인 한 뒤 동시에 요청을 넣어 재생하면 좋지 않았을까 라는 생각이 듭니다.
이렇게 구현시 8-5의 문제도 해결 할 수 있고 (MediaPlayer의 경우 Session Id 제공), 서버의 Ping과 응답받은 데이터의 차이를 통해 미세한 보정또한 할 수 있기 때문입니다.
다만, 이 방식의 경우 현재 유튜브로 재생하는 방식과 달리 저작권 위반의 문제가 있어 조사가 상당히 많이 필요할 것 같습니다.
