# 2주차
## Spring
Java 기반의 대표적인 백엔드 프레임워크로, 객체지향 원칙을 지키면서 개발할 수 있도록 도와준다.

### Spring Boot
> 📌[Spring Boot Initializer 바로가기](https://start.spring.io/)

Spring만 사용하여 프로젝트를 시작하려면 설정이 너무 복잡하였지만 Spring Boot를 이용하면 간단하게 프로젝트를 생성할 수 있다.

즉, 개발자가 설정에 시간을 덜 쓰고, 비즈니스 로직에만 집중할 수 있게 해주는 Spring의 확장 도구이다.

아래는 Spring Boot의 특징을 Chat GPT로 정리하여 표로 나타낸 것이다.

| 기능                            | 설명                                                 |
|-------------------------------|----------------------------------------------------|
| 🔧 자동 설정 (Auto Configuration) | 복잡한 설정 없이, 필요한 설정을 Spring이 자동으로 구성해줌               |
| 🚀 내장 톰캣 지원                   | Tomcat/Jetty/Undertow가 내장되어 있어 따로 설치하지 않고 실행 가능    |
| 📦 스타터 의존성 (Starters)         | `spring-boot-starter-web` 같은 의존성 세트를 한 번에 관리 가능    |
| 📁 독립 실행 JAR 생성               | 실행 가능한 `.jar` 파일로 배포 가능, `java -jar`만으로 서버 실행 가능   |
| 🧪 간편한 테스트 지원                 | `@SpringBootTest`, `@WebMvcTest` 등으로 다양한 테스트 기능 제공 |
| 📊 운영 및 모니터링 기능 내장            | Spring Boot Actuator를 통해 앱 상태 확인, 헬스체크 등 운영 기능 제공  |

## Spring 어플리케이션 구조
![img.png](imgs%2Fimg.png)
1. 사용자가 브라우저에서 특정 URL로 접속
2. 내장 톰켓 서버에서 HTTP 요청을 처리하여 컨트롤러에 전달
3. 전용 컨트롤러(메서드)에서 알맞은 객체를 리턴
4. JSON컨버터에 의해 JSON형식으로 변환 후 HTTP Body에 데이터를 담아 응답

## Spring Bean
> Spring Container가 생성하고 관리하는 객체. 어플리케이션 전체에서 사용하는 공용 객체이다.

- **Spring Container**: Spring Bean이 저장되는 공간. Application Context 라고도 한다.
- 스프링 빈에 등록된 객체에서 특정 빈을 요구하면, 스프링 프레임워크가 자동으로 가져다준다.
- 개발자가 수동으로 Bean 객체를 관리할 수도 있지만(`@Bean`어노테이션 사용) 일반적으로 아래 어노테이션을 사용해 스프링이 관리하도록 하는데, 이 방식을 ***컴포넌트 스캔(Component Scan)*** 이라고 한다.

### 자동 빈 등록(Component Scan)
| 어노테이션        | 설명                                                  | 대표 사용 위치       |
|-------------------|-------------------------------------------------------|------------------------|
| `@Component`      | 가장 기본적인 스프링 빈 등록 어노테이션               | 일반 클래스           |
| `@Controller`     | 웹 요청을 처리하는 컨트롤러로 등록                   | MVC 컨트롤러 클래스   |
| `@Service`        | 비즈니스 로직을 처리하는 서비스 계층에 사용           | 서비스 클래스         |
| `@Repository`     | 데이터 접근 계층에 사용. 예외 처리 등 추가 기능 제공   | DAO, Repository 클래스|

Component Scan 방식으로 스프링 빈으로 등록된 객체들은 스프링이 의존성 주입을 대신 해준다.

- ##### 예시 코드
    ```java
    @Component
    public class MemberService {
        public void join() {
            ...
        }
    }
    ```
    ```java
    @Autowired //Spring Bean에서 의존성 주입
    private MemberService memberService;
    ```

### 수동 빈 등록(`@Configuration` + `@Bean` 조합 사용)
```java
@Configuration
public class TestConfig {

  @Bean
  public MyBean myBean() {
    return new MyBean();
  }
}
```
1. `@Configuration` 어노테이션으로 빈 설정을 위한 클래스임을 명시한다.
2. `@Bean` 어노테이션이 붙은 메서의 리턴 객체를 빈으로 등록한다.

#### 수동 빈 등록을 사용해야 하는 상황
| 상황                         | 이유 및 설명                                        |
|----------------------------|------------------------------------------------|
| 외부 라이브러리 클래스               | 어노테이션을 수정할 수 없기 때문에 수동으로 등록해야 함                |
| 테스트를 위해 Mock 객체를 주입해야 할 때  | 특정 상황에서 실제 객체 대신 가짜 객체를 사용하고 싶을 때              |
| 빈 이름 또는 순서를 명확하게 지정하고 싶을 때 | 자동 등록보다 명시적으로 제어 가능                            |
| 조건에 따라 다른 객체를 등록해야 할 때     | `@Profile`, `@Conditional` 등과 함께 사용하여 환경 분기 가능 |
| 복잡한 생성 로직이 필요한 객체          | 빌더 패턴, 팩토리 메서드 등으로 복잡한 객체 생성 시 유리              |

### 자동 vs 수동 빈 등록
| 항목         | 자동 등록                               | 수동 등록                                  |
|------------|-------------------------------------|----------------------------------------|
| 등록 방식      | `@Component`, `@Service` 등 어노테이션 사용 | `@Configuration` 클래스 안의 `@Bean` 메서드 사용 |
| 제어 범위      | 스프링이 자동으로 탐지                        | 개발자가 직접 지정                             |
| 코드 명시성     | 간결하지만 동작이 숨어 있을 수 있음                | 명확하고 명시적인 코드                           |
| 커스터마이징 유연성 | 제한적                                 | 생성자 인자, 조건 등 유연하게 설정 가능                |
| 외부 클래스 지원  | 불가능 (어노테이션 불가)                      | 가능 (라이브러리 클래스도 등록 가능)                  |
| 충돌 처리 우선순위 | **자동 등록 빈은 수동 등록 빈에 의해 덮어씀**        | 수동 등록이 항상 우선                           |
**Component Scan** 방식은 메인 메서드의 `@SpringBootApplication` 어노테이션 안에 `@ComponentScan`어노테이션이 존재하여 `@Configuration`을 따로 생성할 필요가 없다.
### Application Context 살펴보기
> 수동 빈 등록 예제 코드 참고

![img_1.png](imgs%2Fimg_1.png)
- `org.springframework.context...`: Spring에서 애플리케이션 구동 시 자동으로 생성해주는 빈. 
이 빈들은 **Spring 프레임워크 내부 로직**, **Spring Boot 자동 설정**, 또는 **기본 설정 지원**을 위해 등록된다.
- `testConfig`: `@Configuration` 클래스
- `myBean`: `@Bean`으로 등록된 객체

#### 모든 빈을 조회하기
```java
public class BeanTest {
    
    ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);

    @Test
    public void getAllBeanTest() {
        // 스프링 컨테이너를 설정 파일 정보를 이용해서 생성하고, 스프링 컨테이너 안에 있는 모든 빈을 조회하는 테스트
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        // context 안에 myBean이 들어있는지 검증
        Assertions.assertThat(context.getBeanDefinitionNames()).contains("myBean");
    }
}
```

#### 특정 빈을 조회하기
```java
@Test
public void getOneBeanTest() {
    MyBean myBean1 = context.getBean(MyBean.class);
    MyBean myBean2 = context.getBean(MyBean.class);

    System.out.println(myBean1);
    System.out.println(myBean2);
    Assertions.assertThat(myBean1).isSameAs(myBean2);
}
```
***-> 스프링 빈은 언제나 같은 객체를 반환한다.***
> 🏷️`isEqualTo`와 `isSameAs`의 차이
> 
> `isEqualTo`는 객체의 내용이 같은지, `isSameAs`는 완전히 같은 인스턴스인지 알고 싶을 때 사용한다.
>```java
>String a = new String("hello");
>String b = new String("hello");
>String c = a;
>
>assertThat(a).isEqualTo(b); // ✅ 통과 (내용이 같음)
>assertThat(a).isSameAs(b); // ❌ 실패 (다른 객체)
>
>assertThat(a).isSameAs(c); // ✅ 통과 (같은 객체 참조)
>```

## 의존성(Dependency)
```java
public class Car {
    
    private Wheel wheel;

    public void move() {
        wheel.roll();
    }
}
```
```java
public class Wheel {
    public void roll() {};
}
```
Car 클래스는 `move()`클래스를 실행시키기 위해 Wheel 객체가 필요하다. 이 때, ***"Car는 Wheel에 의존한다."*** 라고 한다.

### 의존성 주입(Dependency Injection)
> 객체가 필요로 하는 의존 객체를 외부에서 대신 주입해주는 설계 방식

1. 스프링이 애플리케이션을 시작하면서 빈 객체들을 찾아 스프링 컨테이너 빈으로 등록한다.
2. 생성자, 필드 등을 분석해 필요한 의존성 찾아 컨테이너에 있는 빈을 찾아 주입해준다.

의존성을 주입할 경우 OCP 원칙을 준수해 유지보수가 용이하고 모든 빈을 싱글톤으로 관리해 메모리를 효율적으로 사용할 수 있다.
> 🏷️ 의존성 주입은 **생성자 주입**, **필드 주입**, **세터 주입** 등이 있지만 가장 권장되는 방식은 **생성자 주입** 방식이다.

#### 생성자 주입
```java
@Getter
@Component
public class MyBean {

    private final MySubBean mySubBean;

    @Autowired //생략 가능
    public MyBean(MySubBean mySubBean) {
        this.mySubBean = mySubBean;
    }
}
```
- `@Autowired`어노테이션을 통해 스프링에 의해 의존성이 주입된다는 것을 명시할 수 있다.
- 생성자가 하나라면 `@Autowired`을 생략 가능하다.
```java
@Getter
@Component
@RequiredArgsConstructor
public class MyBean {

  private final MySubBean mySubBean;
}
```
- Lombok이 제공하는 `@RequiredArgsConstructor` 어노테이션은 모든 final 필드에 대해 생성자를 자동으로 생성해준다.

즉, 생성자 주입을 사용하려면
1. 필요한 의존성을 `final` 키워드를 이용해 추가한다.
2. `@RequiredArgsConstructor` 을 사용해 생성자를 추가한다.

#### 필드 주입
- 필드에 바로 `@Autowired` 를 사용해 의존성을 주입하는 방식으로, 주로 테스트 코드에서 사용된다.
- 필드 주입을 사용하려면, 코드를 실행하기 전 이미 스프링 컨테이너가 존재해 모든 빈이 관리되고 있어야 한다.
> 🏷️ `@SpringBootTest` 어노테이션을 사용하면, 해당 테스트를 실행하기 전에 스프링 부트를 실행시켜 모든 빈을 컨테이너에 넣을 수 있다. 

## Spring Layered Architecture
![img_2.png](imgs%2Fimg_2.png)

### Controller
- 클라이언트의 요청을 받아 응답을 내보내는 계층
- **DTO(Data Transfer Object)를** 사용하여 서비스 계층과 데이터를 주고받는다.(서비스 계층에 의존한다)

### Service
- 어플리케이션의 **비즈니스 로직**이 담기는 계층
- Repository 계층과 소통하며 **Entity**, **DTO**로 소통한다.

### Repository
- DB와 소통하며 데이터를 조작하는 계층
- 서비스 계층에 의해 결정된 비즈니스 로직을 실제 DB에 반영한다.

> ***Controller, Service, Repository는 모두 Spring Bean으로 등록해 의존성을 관리한다.***
