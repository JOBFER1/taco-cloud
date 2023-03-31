package tacos.data;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import tacos.Taco;

public interface TacoRepository extends PagingAndSortingRepository<Taco, Long> {

	Optional<Taco> findById(Long id);

	Taco save(Taco taco);

	void deleteById(Long orderId);

}