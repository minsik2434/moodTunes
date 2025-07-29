# MoodTunes API 플랫폼
사용자가 감정에 따라 음악을 추천받고, 애플리케이션 등록 , 관리 및 API 쿼터를 설정할 수 있는 RESTful API 서비스 입니다


---

## 목차

1. [프로젝트 개요](#프로젝트-개요)
2. [기술 스택](#기술-스택)
3. [주요 기능](#주요-기능)
4. [환경 변수 및 설정](#환경-변수-및-설정)
5. [API](#api)
  - [인증](#인증)
  - [엔트포인트](#엔드포인트)
  - [요청 예시](#요청-예시)
  - [응답 예시](#응답-예시)

--- 

## 프로젝트 개요
- **프로젝트명 :** MoodTunes
- **목표** : 분위기에 맞는 음악을 추천해주는 API 서버 구축, 사용자가 원하는 분위기를 요청하면 그에 맞는 음악을 찾아 제공하는 서비스
- **배경**
    - 많은 음악이 나오고 다양한 챗봇이나 커뮤니티 서비스에 사용자에게 맞춤 콘텐츠를 제공하려는 수요가 증가
    - 서비스에서 무드에 맞는 음악이나 콘텐츠 제공이 중요한 사용자 경험이 됨
    - 분위기에 따른 음악을 추천해 간단하게 활용할 수 있는 API 가 필요함
- **대상** : 외부 앱이 사용할 API

## 기술 스택

- **Spring boot**
- **JPA**
- **MySql**,
- **Redis**

## 주요 기능
- **애플리케이션 등록** : 클라이언트는 API를 사용할 앱을 등록한다
- **API 인증 :**  사용자는 앱을 등록하면 음악을 추천받을 수 있는 API 키를 발급 받는다
- **분위기 별 음악 추천 :** 클라이언트는 자신이 원하는 분위기에 대한 음악을 요청하면 그에 따른 음악을 추천받을 수 있다
- **요청 제한 :** 클라이언트에게 발급된 API 키는 제한된 요청 사용량을 가짐
- **횟수 확인** : 클라이언트는 자신에게 발급된 API 키의 사용량과 남은 횟수를 확인할 수 있음

## 환경 변수 및 설정
| 변수 이름                       | 설명                       | 예시                                 |
|------------------------------|--------------------------|-------------------------------------|
| SPRING_DATASOURCE_URL        | MySQL 연결 URL            | jdbc:mysql://localhost:3306/mood_tunes |
| SPRING_DATASOURCE_USERNAME   | DB 사용자명               | admin                               |
| SPRING_DATASOURCE_PASSWORD   | DB 비밀번호               | password                            |
| SPRING_REDIS_HOST            | Redis 호스트              | localhost                           |
| SPRING_REDIS_PORT            | Redis 포트                | 6379                                |
| APP_DEFAULT_QUOTA_LIMIT      | 시간별 기본 쿼터(요청 수) | 1000                                |


## API

### 인증

- 인증 방식 : API Keys 인증
- 인증 흐름
    1. 클라이언트는  `X-API-KEY` 헤더에 발급받은 API Key를 포함해 요청을 보냄
    2. API 서버에서는 filter 에서 헤더값을 추출해 데이터베이스에 유효한 키인지 확인
        1. 키가 없거나 유효하지 않으면 바로 에러 응답을 보냄
    3. 키가 유효하면 Redis에서 일일 사용량을 체크해서 제한 초과 여부를 판단
        1. 초과시 `429 Too Many Requests` 응답을 보냄
    4. 모든 검증을 통과하면 비지니스 로직으로 요청을 수행
 
- 헤더

| 이름 | 타입 | 필수/선택 | 설명 |
| --- | --- | --- | --- |
| `X-API-KEY`  | `String` | `필수`  | 발급된 API Key 값 |

## 에러 처리

- 공통 응답 형식
    
    ```json
    {
    		"status" : <HTTP 응답 코드>
    		"error" : <Error 코드>
    		"message" : <상세 설명>
    }
    ```
    
## 엔드포인트


| NO | 엔드포인트 | Method | 설명 | 인증 필요 | 요청 Body / Query / Path |
| --- | --- | --- | --- | --- | --- |
| 1 | `/apps` | POST | 앱 등록 & API 키 발급 | ❌ | Body :`{ appName, ownerEmail, desiredQuota}` |
| 2 | `/music` | GET | 기분별 음악 추천  | ✅ | Query : `mood` |
| 3 | `/music/random` | GET | 랜덤 음악 추천 | ✅ | 없음 |
| 4 | `/usage` | GET | 남은 쿼터 조회 | **✅** | Header : `X-API-KEY` |
| 5 | `/apps/{appId}` | GET | 앱 정보 조회 | ✅ | Path : `appId` |
| 6 | `/app/{appId}` | PUT | 앱 정보 수정  | ✅ | Path : `appId`
Body : `{desiredQuota, active}` |
| 7 | `/apps/{appId}` | DELETE | 앱 삭제  | **✅** | Path: `appId` |


# 엔드포인트 상세 명세

## 1. 앱 등록 & API Key 발급

- **URL** : `/apps`
- **Method** : POST
- **설명** : 새로운 앱을 등록하고 API Key를 발급
- **Request Body**
    
    ```json
    {
    		"appName" : "string",
    		"ownerEmail" : "string",
    		"desiredQuota" : 100,
    }
    ```
    
- Response (201 Created)
    
    ```json
    {
    		"appId" 1,
    		"apiKey" : "ABCD-EFGH-IJKN",
    		"quotaLimit" : 100,
    		"issuedAt" : "2025-07-24T13:00:00"
    }
    ```
    
- Error:
    - `400 Bad Request`  : 필드 누락 및 형식 오류

## 2. 기분별 음악 추천

- **URL** : `/music`
- **Method** : GET
- **설명** : 지정된 `mood` 에 맞는 음악 1곡을 랜덤으로 추천
- **Reqeust** :
    - **Query Parameter**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | mood | string | 필수 | 분위기 키워드 |
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
- **Response (200 OK)**
    
    ```json
    {
    		"title" : "좋은날",
    		"artist" : "IU",
    		"mood" : "happy",
    		"tags" : ["R&B/Soul", "발라드"]
    		"url" : "https://youtu.be/jeqdYqsrsA0?si=Z9GjubxbM_U0xkKC"
    }
    		
    ```
    
- **Error**
    - `401 Unauthorized` : API Key 누락 / 유효하지 않음
    - `403 Forbidden` : API Key 비활성화
    - `429 Too Many Requests` : 쿼터 초과

## 3. 랜덤 음악 추천

- **URL** : `/music/random`
- **Method** : GET
- **설명** : 전체 곡에서 랜덤으로 한 곡을 추천
- **Request**
    - **QueryParameter :** 없음
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
- **Response (200 OK)**
    
    ```json
    {
    		"title" : "좋은날",
    		"artist" : "IU",
    		"mood" : "happy",
    		"tags" : ["R&B/Soul", "발라드"]
    		"url" : "https://youtu.be/jeqdYqsrsA0?si=Z9GjubxbM_U0xkKC"
    }
    		
    ```
    
- **Error**
    - `401 Unauthorized` : API Key 누락 / 유효하지 않음
    - `403 Forbidden` : API Key 비활성화
    - `429 Too Many Requests` : 쿼터 초과

## 4. 남은 쿼터 조회

- **URL** : `/usage`
- **Method** : GET
- **설명** : API Key 의 남은 쿼터 조회
- **Request**
    - **QueryParameter :** 없음
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
- **Response (200 OK)**
    
    ```json
    {
    		"used" : 45
    		"limit" : 100,
    		"remaining" : 55,
    		"resetAt" : "2025-07-25T00:00:00"
    }
    ```
    
- **Error**
    - `401 Unauthorized`  : API Key 누락/유효하지 않음
    - `403 Forbidden` : API Key 비활성화

## 5. 앱 정보 조회

- **URL** : `/apps/{appId}`
- **Method** : GET
- **설명** : 등록된 앱 정보 조회
- **Request**
    - **PathVariable**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `appId` | Long | 필수 | 등록한 앱 ID |
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
- **Response**
    
    ```json
    {
    		"appId" 1,
    		"name" : "string",
    		"ownerEmail" : "string",
    		"quotaLimit" : 100,
    		"remainingQuota" : 55,
    		"active" : true,
    		"issuedAt" : "2025-07-24T13:00:00"
    }
    ```
    
- **Error**
    - `401 Unauthorized`  : API Key 누락/유효하지 않음
    - `403 Forbidden` : API Key 비활성화

## 6. 앱 정보 수정

- **URL** : `/apps/{appId}`
- **Method** : PUT
- **설명** : 등록된 앱의 쿼터 한도나 활성화 상태를 수정
- **Request**
    - **PathVariable**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `appId` | Long | 필수 | 등록한 앱 ID |
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
    - **Body**
        
        ```json
        {
          "quotaLimit": 200,     
          "active": false,       
        }
        ```
        
- **Response (200 OK)**
    
    ```json
    {
    		"quotaLimit" : 200,
    		"active" : false
    }
    		
    ```
    
- **Error**
    - `400 BadRequest` : 잘못된 파라미터
    - `401 Unauthorized` : API Key  누락/ 유효하지 않음
    - `403 Forbidden`  : 해당 앱이 비활성화 된 경우
    - `404 Not Found`  : `appId` 에 해당하는 앱이 없는 경우

## 7. 앱 정보 삭제

- **URL** : `/apps/{appId}`
- **Method** : DELETE
- **설명** : 등록된 앱 삭제
- **Request**
    - **PathVariable**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `appId` | Long | 필수 | 등록한 앱 ID |
    - **Header**
        
        
        | 이름 | 타입 | 필수 | 설명 |
        | --- | --- | --- | --- |
        | `X-API-KEY` | string | 필수 | 발급된 API Key |
- **Response**
    - NoContent
- Error
    - `401 Unauthorized` : API Key  누락/ 유효하지 않음
    - `404 Not Found`  : `appId` 에 해당하는 앱이 없는 경우

