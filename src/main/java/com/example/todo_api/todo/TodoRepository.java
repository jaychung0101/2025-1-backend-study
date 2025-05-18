package com.example.todo_api.todo;

import com.example.todo_api.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoRepository {

    @PersistenceContext
    private EntityManager em;

    //Create
    public void save(Todo todo) {
        em.persist(todo);
    }

    //Read
    //단건 조회(한 개의 데이터 조회)
    public Todo findById(Long id) {
        return em.find(Todo.class, id);
    }

    //다건 조회(여러 개의 데이터 조회)
    public List<Todo> findAll() {
        return em.createQuery("select t from Todo as t", Todo.class)
                .getResultList();
    }

    //조건 조회(조건에 맞는 데이터만 조회)
    public List<Todo> findAllByMember(Member member) {
        return em.createQuery("select t from Todo as t where t.member = :todo_member", Todo.class)
                .setParameter("todo_member", member)
                .getResultList();
    }

    //Update
    //엔티티 클래스의 필드를 수정하는 메서드를 작성해서 수정하자!

    //Delete
    public void deleteById(Long id) {
        Todo todo = findById(id);
        em.remove(todo);
    }

    //Only for TEST!!!
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
