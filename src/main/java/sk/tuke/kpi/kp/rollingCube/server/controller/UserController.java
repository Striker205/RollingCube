package sk.tuke.kpi.kp.rollingCube.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.kpi.kp.rollingCube.entity.User;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    @Autowired
    private UserService userService;
    private User loggedUser;
    private Boolean signIn = true;
    private Boolean logIn = true;

    @GetMapping("/")
    public String index() {
        return "LogIn";
    }

    @GetMapping("/login")
    public String login(String login, String password) {
        signIn = true;
        if(userService.isValid(login, password)) {
            loggedUser = new User(login, password);
            logIn = true;
            int level;
            try {
                level = userService.loadLevel(login);
                return "redirect:/CubeMaze/accountLoad?level=" + level;
            }catch (Exception e) {
                System.out.println(e);
                return "redirect:/";
            }
        }else {
            logIn = false;
        }

        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logOut() {
        loggedUser = null;

        return "redirect:/CubeMaze";
    }

    @GetMapping("/signin")
    public String signIn(String login, String password) {
        try {
            userService.registration(new User(login, password));
            signIn = true;
            logIn = true;
            userService.saveLevel(login, 1);
            return "redirect:/";
        }catch (Exception e) {
            signIn = false;
            logIn = true;
            return "redirect:/";
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    public boolean isSignIn() {
        return signIn;
    }

    public boolean isLogIn() {
        return logIn;
    }
}
