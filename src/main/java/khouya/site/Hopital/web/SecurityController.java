package khouya.site.Hopital.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {


    @GetMapping("/noAuthorized")
    public String noAuthorized() {
        return "noAuthorized";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
