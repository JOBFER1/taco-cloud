package tacos.web;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Taco;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.IngredientRepository;
import tacos.data.UserRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
	
	private final IngredientRepository ingredientRepo;
	
	private UserRepository userRepo;
	
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepo, UserRepository userRepo) {
		this.ingredientRepo = ingredientRepo;
		this.userRepo = userRepo;
	}
	
	@ModelAttribute(name = "user")
	public User user(Principal principal) {
		String username = principal.getName();
		User user = userRepo.findByUsername(username);
		return user;
	}
	
	@ModelAttribute
	public void addIngredientsToModel(Model model) {
		
		Iterable<Ingredient> ingredients = ingredientRepo.findAll();
		
		Type[] types = Ingredient.Type.values();
		
		for (Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		}
	}
	
	@ModelAttribute(name = "tacoOrder")
	public TacoOrder order() {
		return new TacoOrder();
	}	
	
	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}	

	@GetMapping
	public String showDesignForm(Model model) {
		
		return "design";
	}

	private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Type type) 
	{
		return ((List<Ingredient>) ingredients).stream().filter(x -> x.getType().equals(type)).collect(Collectors.toList());
	}
	
	@PostMapping
	public String processTaco(@Valid @ModelAttribute("taco") Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {

	    if (errors.hasErrors()) {
	        return "design";
	    }
		// Save the taco...
	    tacoOrder.addTaco(taco);
	    
		log.info("Processing taco: " + taco);
		return "redirect:/orders/current";
	}
	
}