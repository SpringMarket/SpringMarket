package candy.repository.candy;

import candy.entity.candy.Candy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface CandyRepository extends JpaRepository<Candy, Long>, CandyRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Candy> findById(Long id);

}
