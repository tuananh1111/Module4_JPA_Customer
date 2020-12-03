package controller;

import model.Customer;
import model.Province;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import service.CustomerService;
import service.ProvinceService;

import java.util.List;

@Controller
public class ProvinceController {
    @Autowired
    private ProvinceService provinceService;

    @GetMapping("/provinces")
    public ModelAndView listProvinces(){
        Iterable<Province> provinces= provinceService.findAll();
        ModelAndView modelAndView= new ModelAndView("/province/list");
        modelAndView.addObject("provinces", provinces);
        return modelAndView;
    }
    @GetMapping("/create-province")
    public ModelAndView showFormCreatePro(){
        ModelAndView modelAndView= new ModelAndView("/province/create");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }
    @PostMapping("/create-province")
    public ModelAndView saveProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);
        ModelAndView modelAndView= new ModelAndView("/province/create");
        modelAndView.addObject("message", "Created Successfully!!!");
        modelAndView.addObject("province",new Province());
        return modelAndView;
    }
    @GetMapping("/edit-province/{id}")
    public ModelAndView showFormEdit(@PathVariable Long id){
        Province province= provinceService.findById(id);
        ModelAndView modelAndView= new ModelAndView("/province/edit");
        modelAndView.addObject("province", province);
        return modelAndView;
    }
    @PostMapping("/edit-province")
    public ModelAndView updateProvince(@ModelAttribute("province") Province province){
        provinceService.save(province);
        ModelAndView modelAndView= new ModelAndView("/province/edit");
        modelAndView.addObject("province", new Province());
        modelAndView.addObject("message","updated province successfully!");
        return modelAndView;
    }
    @GetMapping("/delete-province/{id}")
    public ModelAndView deleteProvince(@PathVariable Long id){
        provinceService.remove(id);
        Iterable<Province> list= provinceService.findAll();
        ModelAndView modelAndView= new ModelAndView("/province/list");
        modelAndView.addObject("provinces", list);
        return modelAndView;
    }
    @Autowired
    private CustomerService customerService;

    @GetMapping("/view-province/{id}")
    public ModelAndView viewProvince(@PathVariable("id") Long id){
        Province province= provinceService.findById(id);
        if (province==null){
            return new ModelAndView("/error.404");
        }
        Iterable<Customer> customers = customerService.findAllByProvince(province);
        ModelAndView modelAndView= new ModelAndView("/province/view");
        modelAndView.addObject("province",province);
        modelAndView.addObject("customers", customers);
        return modelAndView;
    }


}
