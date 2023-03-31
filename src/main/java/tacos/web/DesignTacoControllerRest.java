package tacos.web;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import tacos.Taco;
import tacos.data.TacoRepository;

@Slf4j
@RestController
@RequestMapping(path = "/design_rest", produces = "application/json")
@CrossOrigin(origins = "*")
public class DesignTacoControllerRest {

	private TacoRepository tacoRepo;

	public DesignTacoControllerRest(TacoRepository tacoRepo) {
		this.tacoRepo = tacoRepo;
	}

	@GetMapping("/recent")
	public Iterable<Taco> recentTacos() {
		PageRequest page = PageRequest.of(0, 12, Sort.by("createdAt").descending());
		return tacoRepo.findAll(page).getContent();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id) {
		Optional<Taco> optTaco = tacoRepo.findById(id);
		if (optTaco.isPresent()) {
			return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping(consumes = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public Taco postTaco(@RequestBody Taco taco) {
		return tacoRepo.save(taco);
	}	
	
	@PutMapping(path="/{id}", consumes="application/json")
	public Taco putTaco( @PathVariable("id") Long id, @RequestBody Taco taco) {

	    taco.setId(id);
	    return tacoRepo.save(taco);
	}

	@PatchMapping(path = "/{id}", consumes = "application/json")
	public Taco patchTaco(@PathVariable("id") Long id, @RequestBody Taco patch) {
		
		Taco taco = tacoRepo.findById(id).get();
		
		if (patch.getName() != null) {
			taco.setName(patch.getName());
		}
		
		if (patch.getCreatedAt() != null) {
			taco.setCreatedAt((patch.getCreatedAt()));
		}
		
		if (patch.getIngredients() != null) {
			taco.setIngredients((patch.getIngredients()));
		}

		return tacoRepo.save(taco);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteTaco(@PathVariable("id") Long id) {
		
		tacoRepo.deleteById(id);
	}	
	
}
