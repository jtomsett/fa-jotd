package jtomsett.fa_jotd.controller;

import jtomsett.fa_jotd.dao.Joke;
import jtomsett.fa_jotd.exceptions.JokeNotFoundException;
import jtomsett.fa_jotd.service.JokeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class JokeRestController {

    private final JokeService jokeService;

    @GetMapping("/joke/{id}")
    public Joke getJoke(@PathVariable("id") Long id) {
        return jokeService.getJokeById(id).orElseThrow(() -> new JokeNotFoundException(id));
    }

    @GetMapping("/joke/today")
    public Joke getJoke() {
        LocalDate today = LocalDate.now();
        return jokeService.getJokeByDate(today).orElseThrow(() -> new JokeNotFoundException(today));
    }

    @PostMapping("/joke/add")
    public Joke addJoke(@RequestBody Joke joke) {
        return jokeService.addJoke(joke);
    }

    @PutMapping("/joke/update")
    public Joke updateJoke(@RequestBody Joke joke) {
        return jokeService.updateJoke(joke);
    }

    @DeleteMapping("/joke/delete/{id}")
    public void deleteJoke(@PathVariable("id") Long id) {
        jokeService.deleteJoke(id);
    }
}
