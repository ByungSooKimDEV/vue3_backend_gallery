package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findMemberByEmailAndPassword(String email, String password);

}
