package candy.repository.candy;

import candy.entity.candy.Candy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandyRepository extends JpaRepository<Candy, Long> {

}
