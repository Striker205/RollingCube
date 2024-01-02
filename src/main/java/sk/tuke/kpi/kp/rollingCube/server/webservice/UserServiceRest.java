package sk.tuke.kpi.kp.rollingCube.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.rollingCube.entity.User;
import sk.tuke.kpi.kp.rollingCube.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @PostMapping
    public void registration(@RequestBody User user) {
        userService.registration(user);
    }

    @GetMapping
    public boolean isValid(@RequestParam String login,@RequestParam String password) {
        return userService.isValid(login, password);
    }

    @PutMapping("/{login}")
    public void saveLevel(@PathVariable String login,@RequestParam int level) {
        userService.saveLevel(login, level);
    }

    @GetMapping("/{login}")
    public int loadLevel(@PathVariable String login) {
        return userService.loadLevel(login);
    }
}
