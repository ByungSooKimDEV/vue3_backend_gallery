package gallery.backend.repository;

import gallery.backend.domain.item.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Member findMemberByEmailAndPassword(String email, String password);

}
