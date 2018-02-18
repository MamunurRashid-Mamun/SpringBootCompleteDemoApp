package com.ict.mamun.springbootallfeature.controllers;

import com.ict.mamun.springbootallfeature.entity.Role;
import com.ict.mamun.springbootallfeature.entity.User;
import com.ict.mamun.springbootallfeature.repositories.RoleRepository;
import com.ict.mamun.springbootallfeature.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHome() {
        return "home";
    }

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public String registration(@ModelAttribute("user") User user, Model model) {
//        User tempUser = userRepository.findByUserName(user.getUserName());
//        if (tempUser != null) {
//            model.addAttribute("usernamefounderror","User Name exist, try another");
//            return "registration";
//        }
//        List roles = new ArrayList();
//        //roles.add(new Role("ROLE_ADMIN"));
//        roles.add(new Role("ROLE_USER"));
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//
//        user.setEnabled(true);
//        user.setRoles(roles);
//        userRepository.save(user);
//        return "redirect:./";
//    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userRepository.findByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            List roles = new ArrayList();
            //roles.add(new Role("ROLE_ADMIN"));
            roles.add(new Role("ROLE_USER"));
            System.out.println(user);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

            user.setEnabled(true);
            user.setRoles(roles);
            userRepository.save(user);

            modelAndView.addObject("successMessage", "User has been registered successfully. You can login now.");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/securedData", method = RequestMethod.GET)
    public ModelAndView securedData(Principal principal) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("userName",principal.getName());
        return modelAndView;
    }
}
