package entity.control;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RoleController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String user() {
        return "/page/user";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "/page/admin";
    }
}
