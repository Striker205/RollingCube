package sk.tuke.kpi.kp.rollingCube.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.kpi.kp.rollingCube.entity.Score;
import sk.tuke.kpi.kp.rollingCube.service.ScoreService;


import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreServiceRest {

    @Autowired
    private ScoreService scoreService;

    @GetMapping("/{game}")
    public List<Score> getTopScoresInGame(@PathVariable String game) {
        return scoreService.getTopScoresInGame(game);
    }

    @GetMapping("/{game}/{level}")
    public List<Score> getTopScoresInLevel(@PathVariable String game, @PathVariable int level) {
        return scoreService.getTopScoresInLevel(game, level);
    }

    @PostMapping
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }
}
