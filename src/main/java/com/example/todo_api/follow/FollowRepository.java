package com.example.todo_api.follow;

import com.example.todo_api.member.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowRepository {

    @PersistenceContext
    private EntityManager em;

    //Create
    public void save(Follow follow) {
        em.persist(follow);
    }

    //Read
    public Follow findById(Long id) {
        return em.find(Follow.class, id);
    }

    public List<Follow> findAll() {
        return em.createQuery("select f from Follow as f", Follow.class)
                .getResultList();
    }

    public List<Follow> findAllByFollower(Member member) {
        return em.createQuery("select f from Follow as f where f.follower = :follower_id", Follow.class)
                .setParameter("follower_id", member)
                .getResultList();
    }

    public List<Follow> findAllByFollowing(Member member) {
        return em.createQuery("select f from Follow as f where f.following = :following_id", Follow.class)
                .setParameter("following_id", member)
                .getResultList();
    }

    public Follow findByFollowerAndFollowing(Member follower, Member following) {
        return em.createQuery("select f from Follow as f where f.follower = :follower and f.following = :following", Follow.class)
                .setParameter("follower", follower)
                .setParameter("following", following)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    //Delete
    public void deleteById(Long id) {
        Follow follow = findById(id);
        em.remove(follow);
    }

    public void deleteByFollower(Member member) {
        List<Follow> follows = findAllByFollower(member);
        for (Follow follow : follows) {
            em.remove(follow);
        }
    }

    //only for TEST
    public void flushAndClear() {
        em.flush();
        em.clear();
    }
}
