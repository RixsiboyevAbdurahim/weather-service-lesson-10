package entity.control;

import entity.models.user.AuthUser;
import entity.models.user.AuthUserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SecurityMyController {

    private final AuthUserDao authUserDao;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home() {
        return "/home";
    }

    @PostMapping("/")
    public String homePost() {
        return "/home";
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

        if (password.equals(confirm_password) && !password.isEmpty() && !username.isEmpty()) {
            AuthUser authUser = AuthUser.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .role("USER")
                    .build();
            AuthUser user = authUserDao.findByUsername(username).orElse(null);
            if (user == null) {
                authUserDao.save(authUser);
                System.out.println("You successfully registered!");
                model.addAttribute("register_success", "register_success");
                return "/login";
            } else {
                model.addAttribute("user_exists", "user_exists");
                return "register";
            }
        }

        model.addAttribute("register_failed", "register_failed");
        return "register";
    }

}
