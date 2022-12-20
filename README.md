![waving](https://capsule-render.vercel.app/api?type=waving&height=200&text=🎉아삼이삼_A106팀🎉&fontAlign=50&fontAlignY=40&color=gradient)

# 서비스 - Min'dLet



## 목차

- [팀 소개](#팀-소개)

- [서비스 기획의도](#서비스-기획의도)

- [외부 서비스 정보 관련 문서](#외부 서비스 정보 관련 문서)

- [와이어 프레임](#와이어프레임)

- [Git Convention](#Git Convention)

- [Jira](#Jira)

- [배포시 특이사항(FE, BE, DB)](#배포시 특이사항)

- [실행화면](#실행화면)

  



## 팀 소개

| 구분 | 이름   | 개발 파트  |
| ---- | ------ | :--------: |
| 팀장 | 안영진 |   백엔드   |
| 팀원 | 김경석 |   백엔드   |
| 팀원 | 이선민 | 프론트엔드 |
| 팀원 | 이윤기 |   백엔드   |
| 팀원 | 임건호 | 프론트엔드 |
| 팀원 | 진민규 | 프론트엔드 |

<hr>



## 서비스 기획의도

롤링페이퍼를 통하여 일상에 지친 소비자들에게 잠시 동화적 순간을 선물하여 힘이 되어 주는 것이 우리 웹사이트의 목표입니다. 

평소 마음에 담아왔던 말을 정성스레 적어 민들레 씨앗으로 “후” 불어낸 뒤 다른 사람에게 위로를 받기도, 조언을 받기도 하는 것입니다.

<hr>


## 서비스 주요기능

- ### 롤링페이퍼 시작을 위한 씨앗 날리기

  로그인 후 랜딩페이지에서 아래에서 위로 스와이프 하면 나오는 화면. 이를 통해 롤링페이퍼를 시작할 수 있고, 시작된 롤링페이퍼는 다른 이에게 랜덤으로 확인되어 이어 써지게 됩니다. 전송하려면 특색있게도 마이크를 이용하여 “후” 바람을 불어야 합니다.

- ### 다른 이의 롤링페이퍼에 이어 작성하기 위한 랜덤 씨앗 확인

  롤링페이퍼를 완성하기 위하여 랜덤으로 다른 이의 씨앗에 접근하여 이어 쓰는 기능입니다.

- ### 꽃밭 확인

  자신에게 돌아온 롤링페이퍼를 확인할 수 있습니다. 총 5개까지 씨앗을 날릴 수 있기에 5개의 팻말이 있고, 앨범에 보관할 수도 있습니다.

<hr>


## 외부 서비스 정보 관련 문서

- AWS RDS

  => AWS RDS 데이터베이스를 사용합니다.

- AWS S3

  => AWS S3 파일 스토리지를 사용합니다.

---



## 와이어 프레임

- 와이어 프레임

  ![와이어프레임](https://user-images.githubusercontent.com/73927750/169438616-3fdace96-e3fa-4ddc-80b1-1d9d3a6af4f7.png)

- 사이트맵

  ![사이트맵](https://user-images.githubusercontent.com/73927750/169438621-c9e55b83-9f23-4fb3-8274-76e7647046cf.png)

<hr>



## Git Convention

- Git 커밋 컨벤션

  ```shell
  $ git checkout -b 'branch-name'
  
  $ git checkout -b (front or back)/Add-Login-API/#3EWIAOVAIE
  
  $ git checkout -b develop:(front or back)/유형-기능요약/jira이슈번호
  	          |-----| 는 파고싶은 브랜치 이름
  
  $ git branch checkout -b front/feature-login-menu-component/#ASDWQ123
  		         |------------------------------------------| (브랜치 이름)
  $ git push 라고 치면 어떻게 쳐야하는지 알아서 알려줌
  
  $ git push origin front-feature/#ASDWQ123-login-menu-component
  		  |------------------------------------------| (브랜치 이름)
  
  /////////////////////////////////////////////////////////////////////////////////
  [보낼때]
  $ git add .
  
  $ git commit -m "#S06P12A202-80 Feat: Component 생성"
  
  $ git push origin develop:front/feature-nav-component/#S06P12A202-56
  
  $ git push origin develop:front/fest-home-funding-list/#S06P12A202-82
  
  [받을때]
  $ git pull origin develop
  ```

- branch-name 양식

  ```
  develop(front or back)/유형-기능요약/Jira 이슈번호
  ```


<hr>



## Jira

- Story 제목은 아래 형식으로 작성한다.
  ```text
  [BE: 댓글 등록] API 개발
  [FE: 댓글 등록] 게시글에 댓글 등록 기능 개발
  ```
  
- Labels는 대문자로 작성

- Labels에 업무 직무는 FRONTEND 또는 BACKEND로 작성

- Labels에 업무 형태는 Git 커밋 메세지와 동일(ex. FEAT, FIX, DOCS 등)   

<hr>



## 배포 시 특이사항

- Frontend

  ```bash
  # 소스코드 클론
  git clone https://lab.ssafy.com/s06-final/S06P31A106.git
  
  # Frontend 디렉터리 이동
  cd frontend
  
  # 라이브러리 다운로드
  npm install
  
  # 실행
  npm run start
  ```

  

- Backend

  ```bash
  # 소스코드 클론
  git clone https://lab.ssafy.com/s06-final/S06P31A106.git
  
  # Backend 디렉터리 이동
  cd backend
  
  # gradlew 권한 부여
  chmod u+x ./gradlew
  
  # 빌드
  ./gradlew clean build
  
  # 도커 이미지 생성
  docker build -t backend .
  
  # 컨테이너 실행
  docker run -d -it -p 8080:8080 --name backend backend
  ```



- Database

  - 데이터베이스의 경우 AWS RDS를 사용하고 있어 별도의 데이터베이스 구축이 필요합니다.

  ```bash
  # 데이터베이스 이미지 다운로드
  docker pull mariadb
  
  # 컨테이너 실행
  docker run -d -it -p 3306:3306 -e MYSQL_ROOT_PASSWORD='password' --name mindlet-mariadb
  
  # 컨테이너 내부 접속
  docker exec -it mindlet-mariadb /bin/bash
  
  # 데이터베이스 접속
  mysql -u root -p
  ```

  ```bash
  -- 데이터베이스 생성
  create database mindlet default character set utf8mb4 collate utf8mb4_general_ci;
  
  -- 계정 생성
  create user 'your-id'@'%' identified by 'your-password';
  
  -- 권한 부여
  grant all privileges on mindlet.* TO 'your-id'@'%';
  flush privileges;
  ```

  - 파일 스토리지의 경우 AWS S3를 사용하고 있어 별도의 파일 스토리지 구축이 필요합니다.

  ```bash
  # 파일 디렉터리 생성
  mkdir -p 'file storage path'/static/files/images/{content, nation}
  ```

  

<hr>



## 실행 화면 일부

### 1. 로그인 

![로그인](https://user-images.githubusercontent.com/73927750/169356257-51e6933f-e294-44d7-9af3-719b710e64d5.png)



### 2. 회원가입

![회원가입](https://user-images.githubusercontent.com/73927750/169356267-f8cd46b8-c396-4584-b6b5-ac6d76af5ca0.png)



### 3. 랜딩 페이지

![랜딩페이지](https://user-images.githubusercontent.com/73927750/169357123-9b28038d-cad8-4183-b5fc-44d6fff63d0d.png)



### 4. 민들레씨 잡기 애니메이션

![민들레씨잡기애니](https://user-images.githubusercontent.com/73927750/169357139-62add70a-d7aa-4038-b174-9fa4148a6170.png)



### 5. 잡은 민들레 보기 화면

![민들레메세지](https://user-images.githubusercontent.com/73927750/169357182-4fbbc1fe-8b56-4a42-ab67-a90a2e763ccb.png)



### 6. 민들레 씨 작성

![민들레씨작성](https://user-images.githubusercontent.com/73927750/169357193-89cab000-590e-49c8-9f0d-60bc0dbecdbf.png)



### 7. 민들레 씨 보내기 후 애니메이션

![민들레씨작성후애니](https://user-images.githubusercontent.com/73927750/169357196-8e8c8b57-a4b2-488f-a968-74694621996d.png)



### 8. 민들레 씨 불기 애니메이션, 마이크

![민들레불기애니](https://user-images.githubusercontent.com/73927750/169357191-a2dfc30c-6307-41dd-b00d-9b13476e31e0.png)



### 9. 꽃밭 페이지

![꽃밭](https://user-images.githubusercontent.com/73927750/169358967-f64b4006-49fb-45ad-b8ec-3b63b0c796ab.png)





### 10. 보관함 페이지

![보관함](https://user-images.githubusercontent.com/73927750/169358969-958633bb-0481-4742-a7de-3c7423625190.png)

## 

### 11. 설정페이지

![설정](https://user-images.githubusercontent.com/73927750/169358973-02ba90c1-81e6-4392-9ccf-5fad079e9667.png)
