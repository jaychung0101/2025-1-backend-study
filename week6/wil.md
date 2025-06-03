# 6주차
## 유효성 검증
- 요청으로 들어오는 데이터가 올바른 형식인지 검사하는 것
- 데이터를 받아들이는 DTO에서 유효성을 검사한다.  
  이 때, 유효성 검증은 ***형식과 값 자체의 유효성만 판단*** 한다. DB에 존재하는지 여부, 중복 검사 등은 비즈니스 로직에서 따로 검증해야 한다.

```java
implementation 'org.springframework.boot:spring-boot-starter-validation'
```
아래 유효성 검사를 사용하기 위해서는 위 의존성을 추가해야 한다.
```java
@Getter
public class TodoCreateRequest {

    @Length(max = 200, message = "content 길이는 100자 입니다.")
    private String content;
    
    @NotNull(message = "member id 값은 필수입니다.")
    private Long memberId;
}
```
```java
@PostMapping
public ResponseEntity<Void> createTodo(@RequestBody @Valid TodoCreateRequest request) {...}
```
`@Length`, `@NotNull`과 같은 어노테이션을 사용해 해당 필드에 제한을 줄 수 있고, 유효성 검사를 통과하지 못 할 경우 출력할 메세지를 message에 넣는다.  
이후, 해당 DTO를 사용하는 컨트롤러에 `@Valid` 어노테이션을 추가하면 유효성 검사가 실행된다.  

- content 길이가 200을 초과한 경우 나오는 에러 메세지
```json
{
  "timestamp": "2025-06-03T08:46:10.213+00:00",
  "status": 400,
  "error": "Bad Request",
  "path": "/todo"
}
```
하지만 여전히 왜 이런 에러가 나오는지 정확하게 알기 힘들다.

> #### 자주 쓰이는 유효성 검사 어노테이션
| 어노테이션               | 설명                                   | 적용 대상                 |
|---------------------|--------------------------------------|-----------------------|
| `@NotNull`          | `null` 금지 (빈 문자열은 허용)                | 모든 타입                 |
| `@NotEmpty`         | `null` 및 빈 컨테이너(`""`, `[]`, `{}`) 금지 | 문자열, 컬렉션 등            |
| `@NotBlank`         | `null` 및 공백 문자열 금지 (`" "`, `"\n"`)   | 문자열                   |
| `@Size(min, max)`   | 문자열/배열/컬렉션의 크기 제한                    | 문자열, 배열, 컬렉션          |
| `@Length(min, max)` | 문자열 길이 제한 (Hibernate 전용)             | 문자열 (Hibernate 필요)    |
| `@Min(value)`       | 최소값 제한 (정수형에 사용)                     | 숫자형 (`int`, `long`)   |
| `@Max(value)`       | 최대값 제한                               | 숫자형 (`int`, `long`)   |
| `@Positive`         | 양수 여부 검사 (`> 0`)                     | 숫자형                   |
| `@PositiveOrZero`   | 0 이상 검사 (`>= 0`)                     | 숫자형                   |
| `@Negative`         | 음수 여부 검사 (`< 0`)                     | 숫자형                   |
| `@NegativeOrZero`   | 0 이하 검사 (`<= 0`)                     | 숫자형                   |
| `@Email`            | 이메일 형식 검사                            | 문자열                   |
| `@Pattern(regexp)`  | 정규 표현식과 일치하는지 검사                     | 문자열                   |
| `@AssertTrue`       | `true`인지 검사                          | `boolean`, `Boolean`  |
| `@AssertFalse`      | `false`인지 검사                         | `boolean`, `Boolean`  |
| `@Future`           | 현재보다 미래의 날짜/시간인지 검사                  | `Date`, `LocalDate` 등 |
| `@Past`             | 현재보다 과거의 날짜/시간인지 검사                  | `Date`, `LocalDate` 등 |

### Global Exception Handler
예외 종류에 따라 응답할 response를 설정할 수 있다. 이름 그대로, 스프링 어플리케이션 전역에서 발생하는 모든 에러에 대한 처리를 결정한다.

```java
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handlerException(Exception e) {
    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
  }
}
```
`@ControllerAdvice`는 모든 컨트롤러에서 발생하는 예외를 가로채 처리하도록 하는 어노테이션이다.
```java
@Getter
@AllArgsConstructor
public class ErrorResponse {
  private String message;
}
```
`@ExceptionHandler`는 특정 예외 클래스에 대한 처리 로직을 지정하도록 하는 어노테이션이다.

- 스프링 어플리케이션에서 에러가 발생하면, 해당 에러 타입에 대한 핸들러가 기존 컨트롤러 대신 `response body`를 생성해 응답한다.
  위 코드에선 `ErrorResponse`가 `ResponseEntity`로 들어가게 된다.
- 에러 클래스를 매칭할 때엔 예외 객체의 상속관계를 따라 올라가며 매칭된다.  
  예를 들어, `Exception`은 모든 에러 클래스의 공통 부모이므로, 특정 핸들러로 처리하지 못한 에러는 모두 `Exception`이 처리해준다.

#### 예외 클래스 매칭 순서 정리
1. 가장 구체적인 예외 클래스부터 찾아서 처리
2. 해당 예외 클래스가 없으면 상위 클래스 (`RuntimeException`, `Exception`)로 올라가며 매칭
3. `@ExceptionHandler(Exception.class)`는 최후의 보루 역할

### AOP(Aspect-Oriented Programming; 관점 지향 프로그래밍)
객체 지향 프로그래밍을 보완하는 개념으로, **공통 관심사(Cross-Cutting Concern)를 모듈화**하여 코드 중복을 줄이고, 핵심 로직과 분리하는 프로그래밍 패러다임
- Aspect: 공통 기능을 정의한 모듈  
  ex. 로깅, 트랜잭션 처리, 보안, 예외 처리 등
- Joint Point: Advice가 적용될 수 있는 실행 지점 (Spring에서는 메서드 실행 시점)  
  ex. User Controller에서 에러가 발생했을 경우 Joint Point는 에러가 발생한 메서드
- Advice: Join Point에 삽입될 실제 코드  
  ex. Global Exception Handler가 작동하여 해당 Joint Point의 에러를 처리한다.

### API 문서화(Swagger)
```java
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8'
```
위 의존성을 추가하면 [이곳](http://localhost:8080/swagger-ui/index.html)에서 자동으로 생성된 API문서를 열람할 수 있다.