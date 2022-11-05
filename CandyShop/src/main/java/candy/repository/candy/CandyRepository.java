package candy.repository.candy;

import candy.entity.candy.Candy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CandyRepository extends JpaRepository<Candy, Long>, CandyRepositoryCustom {

    List<Candy> findAllByViewDesc();
}
