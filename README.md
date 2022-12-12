# 민들렛
> 랜덤한 사람들과 다양한 컨텐츠를 주고받을수 있는 타임캡슐형식의 롤링페이퍼 SNS

<div align="center">
<img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
<img src="https://img.shields.io/badge/spring data JPA-092E20?style=for-the-badge&logo=springdataJPA&logoColor=white"/>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"/>
<img src="https://img.shields.io/badge/queryDSL-0085CE?style=for-the-badge&logo=queryDSL&logoColor=white"/>
<img src="https://img.shields.io/badge/mariaDB-007396?style=for-the-badge&logo=mariaDB&logoColor=white"/>
</div>

<div>
Min’dLet은 게시물을 작성하고 돌아오는 날짜를 선택하면 여러 사람들이 게시물에 반응을 남겨주고 지정한 날짜에 다시 작성자에게 돌아오는 타임캡슐 롤링페이퍼 형식의 SNS 입니다. 작성자가 민들레에 글을 써서 민들레를 불면 떠다니면서 많은 사람들에게 접촉하고 지정한 날짜에 다시 작성자의 꽃밭으로 돌아와 내용을 확인할수 있는 스토리 설정이 가능.

## 동작 영상

https://user-images.githubusercontent.com/37072014/206997393-c6087515-ad21-44f5-8758-662210008542.mp4




## 사용 방법

URL 접속 : ```https://min-dlet.ssafy.io/login```


## 사용 예제

스크린 샷과 코드 예제를 통해 사용 방법을 자세히 설명합니다.

_더 많은 예제와 사용법은 [Wiki][wiki]를 참고하세요._

## 개발 환경 설정

모든 개발 의존성 설치 방법과 자동 테스트 슈트 실행 방법을 운영체제 별로 작성합니다.

```sh
make install
npm test
```

## 업데이트 내역

* 0.2.1
    * 수정: 문서 업데이트 (모듈 코드 동일)
* 0.2.0
    * 수정: `setDefaultXYZ()` 메서드 제거
    * 추가: `init()` 메서드 추가
* 0.1.1
    * 버그 수정: `baz()` 메서드 호출 시 부팅되지 않는 현상 (@컨트리뷰터 감사합니다!)
* 0.1.0
    * 첫 출시
    * 수정: `foo()` 메서드 네이밍을 `bar()`로 수정
* 0.0.1
    * 작업 진행 중

## 정보

이름 – [@트위터 주소](https://twitter.com/dbader_org) – 이메일주소@example.com

XYZ 라이센스를 준수하며 ``LICENSE``에서 자세한 정보를 확인할 수 있습니다.

[https://github.com/yourname/github-link](https://github.com/dbader/)

## 기여 방법

1. (<https://github.com/yourname/yourproject/fork>)을 포크합니다.
2. (`git checkout -b feature/fooBar`) 명령어로 새 브랜치를 만드세요.
3. (`git commit -am 'Add some fooBar'`) 명령어로 커밋하세요.
4. (`git push origin feature/fooBar`) 명령어로 브랜치에 푸시하세요. 
5. 풀리퀘스트를 보내주세요.

<!-- Markdown link & img dfn's -->
[npm-image]: https://img.shields.io/npm/v/datadog-metrics.svg?style=flat-square
[npm-url]: https://npmjs.org/package/datadog-metrics
[npm-downloads]: https://img.shields.io/npm/dm/datadog-metrics.svg?style=flat-square
[travis-image]: https://img.shields.io/travis/dbader/node-datadog-metrics/master.svg?style=flat-square
[travis-url]: https://travis-ci.org/dbader/node-datadog-metrics
[wiki]: https://github.com/yourname/yourproject/wiki
