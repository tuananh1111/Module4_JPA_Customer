package controller;

import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import service.CustomerService;
import service.ProvinceService;

import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProvinceService provinceService;

    @ModelAttribute("provinces")
    public Iterable<Province> provinces(){
        return provinceService.findAll();
    }

    @GetMapping("/create-customer")
    public ModelAndView showCreateForm(){
        ModelAndView modelAndView= new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
//        Iterable<Province> provinces= provinceService.findAll();
//        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }
    @PostMapping("/create-customer")
    public ModelAndView saveCustomer(@Validated @ModelAttribute("customer")Customer customer, BindingResult bindingResult){
        if (bindingResult.hasFieldErrors()){
            ModelAndView modelAndView= new ModelAndView("/customer/create");
            return modelAndView;
        }

        customerService.save(customer);
        ModelAndView modelAndView= new ModelAndView("/customer/create");
        modelAndView.addObject("customer", new Customer());
        modelAndView.addObject("message","New customer created successfully");
        return modelAndView;
    }
    @GetMapping("/customers")
    public ModelAndView listCustomer(@RequestParam("s")Optional<String> s, Pageable pageable){
        Page<Customer> list;
        if (s.isPresent()){
            list= customerService.findAllByFirstNameContaining(s.get(),pageable);
        }else {
            list= customerService.findAll(pageable);
        }
        ModelAndView modelAndView= new ModelAndView("/customer/list");
        modelAndView.addObject("customers", list);
        return modelAndView;
    }
    @GetMapping("/edit-customer/{id}")
    public ModelAndView showEditForm(@PathVariable Long id){
        Customer customer= customerService.findById(id);
        if (customer!=null){
            ModelAndView modelAndView= new ModelAndView("/customer/edit");
            modelAndView.addObject("customer", customer);
            return modelAndView;
        }else {
            ModelAndView modelAndView= new ModelAndView("/error.404");
            return modelAndView;
        }
    }
    @PostMapping("/edit-customer")
    public ModelAndView updateCustomer(@ModelAttribute("customer") Customer customer){
        customerService.save(customer);
        ModelAndView modelAndView= new ModelAndView("/customer/edit");
//        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Customer updated successfully");
        return modelAndView;
    }
    @GetMapping("/delete-customer/{id}")
    public ModelAndView deleteCustomer(@PathVariable long id, Pageable pageable){
        customerService.remove(id);
        Page<Customer> list= customerService.findAll(pageable);
        ModelAndView modelAndView= new ModelAndView("/customer/list");
        modelAndView.addObject("customers",list);
        return modelAndView;
    }
}
