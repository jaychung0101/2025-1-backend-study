package com.example.todo_api.todo;

import com.example.todo_api.member.Member;
import com.example.todo_api.member.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT) //실제 8080 포트를 이용해 테스트
public class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @Transactional //트랜잭션 단위로 실행
    @Rollback(value = false) //수동으로 롤백을 꺼줌
    void todoSaveTest() {
        // 트랜잭션 시작
        Todo todo = new Todo("todo content", false, null);
        todoRepository.save(todo);
        //트랜잭션 종료 -> 커밋
        //에러가 발생할 경우 자동으로 롤백

        Assertions.assertThat(todo.getId()).isNotNull();
        //테스트가 끝나도 자동으로 롤백
    }

    @Test
    @Transactional
    void todoFindOneByIdTest() {
        //given
        Todo todo = new Todo("todo content", false, null);
        todoRepository.save(todo);

        todoRepository.flushAndClear(); // 영속성 컨텍스트 반영 및 클리어

        //when
        Todo findTodo = todoRepository.findById(todo.getId());

        //then
        Assertions.assertThat(findTodo.getId()).isEqualTo(todo.getId());
    }

    @Test
    @Transactional
    void todoFindAllTest() {
        //given
        Todo todo1 = new Todo("todo content1", false, null);
        Todo todo2 = new Todo("todo content2", false, null);
        Todo todo3 = new Todo("todo content3", false, null);

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        //when
        List<Todo> todoList = todoRepository.findAll();

        //then
        Assertions.assertThat(todoList).hasSize(3);
    }

    @Test
    @Transactional
    void todoFindAllByMember() {
        //given
        Member member1 = new Member();
        Member member2 = new Member();

        memberRepository.save(member1);
        memberRepository.save(member2);

        Todo todo1 = new Todo("todo content1", false, member1);
        Todo todo2 = new Todo("todo content1", false, member1);
        Todo todo3 = new Todo("todo content2", false, member2);

        todoRepository.save(todo1);
        todoRepository.save(todo2);
        todoRepository.save(todo3);

        List<Todo> member1TodoList = todoRepository.findAllByMember(member1);
        List<Todo> member2TodoList = todoRepository.findAllByMember(member2);

        Assertions.assertThat(member1TodoList).hasSize(2);
        Assertions.assertThat(member2TodoList).hasSize(1);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void todoUpdateTest() {
        Todo todo1 = new Todo("todo content1", false, null);
        todoRepository.save(todo1);

        todoRepository.flushAndClear();

        Todo findTodo1 = todoRepository.findById(todo1.getId());
        findTodo1.updateContent("newContent");

        Assertions.assertThat(findTodo1.getContent()).isEqualTo("newContent");
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void todoDeleteTest() {
        Todo todo1 = new Todo("todo content1", false, null);
        Todo todo2 = new Todo("todo content2", false, null);
        todoRepository.save(todo1);
        todoRepository.save(todo2);

        todoRepository.flushAndClear();

        todoRepository.deleteById(todo1.getId());
    }

    @AfterAll
    public static void doNotFinish() {
        System.out.println("test finished");

        //H2 DB는 in-memory이기 때문에 프로그램 실행이 종료되면 모두 날아감
        while (true) {}
    }
}
