package tacos.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import tacos.TacoOrder;
import tacos.User;
import tacos.data.OrderRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {
	
	private OrderRepository orderRepo;
	
	public OrderController(OrderRepository orderRepo) {
		this.orderRepo = orderRepo;
	}
	
	@GetMapping("/current")
	public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute TacoOrder tacoOrder) {	
		
	    if (tacoOrder.getDeliveryName() == null) {
	    	tacoOrder.setDeliveryName(user.getFullname());
	      }
	      if (tacoOrder.getDeliveryStreet() == null) {
	    	  tacoOrder.setDeliveryStreet(user.getStreet());
	      }
	      if (tacoOrder.getDeliveryCity() == null) {
	    	  tacoOrder.setDeliveryCity(user.getCity());
	      }
	      if (tacoOrder.getDeliveryState() == null) {
	    	  tacoOrder.setDeliveryState(user.getState());
	      }
	      if (tacoOrder.getDeliveryZip() == null) {
	    	  tacoOrder.setDeliveryZip(user.getZip());
	      }
	      
		return "orderForm";
	}
	
	@PostMapping
	public String processOrder(@Valid TacoOrder tacoOrder, Errors errors, SessionStatus sessionStatus, 
								@AuthenticationPrincipal User user) {
		
	    if (errors.hasErrors()) {
	        return "orderForm";
	      }
	    
		log.info("Order submitted: " + tacoOrder);
		
		tacoOrder.setUser(user);
		
		orderRepo.save(tacoOrder);
		sessionStatus.setComplete();
		
		return "redirect:/";
	}
}