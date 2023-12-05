package prog.academy.infomoney.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import prog.academy.infomoney.entity.Token;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t join fetch User u
            on t.user.id = u.id
            where u.id = :tokenId and (t.expired = false or t.revoked = false)
            """)
    List<Token> findAllValidTokenByUser(@Param("tokenId") Long id);

    Optional<Token> findByJwtToken(String token);
}
