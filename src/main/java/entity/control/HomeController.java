package entity.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @GetMapping("")
    public String home() {
        return "home";
    }

    @PostMapping("")
    public String postHome() {
        return "home";
    }


    @GetMapping("/login")
    public String pageLogin(
            @RequestParam(required = false, name = "error") String error,
            @RequestParam(required = false, name = "logout") String logout,
            Model model) {
        if (error != null) model.addAttribute("error", "error_badcredantials");
        if (logout != null) model.addAttribute("logout", "logout_message");
        return "login";
    }

    @PostMapping("/login")
    public String postLogin() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirm_password") String confirm_password,
            Model model
    ) {

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("confirm_password = " + confirm_password);

        if (password.equals(confirm_password)) return "redirect:/login";

        model.addAttribute("register_failed", "register_failed");
        return "register";
    }

}
