package jtomsett.fa_jotd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
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

    @Operation(summary = "Find a joke by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the joke", content = {
                    @Content(schema = @Schema(implementation = Joke.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Joke not found for provided id.", content = @Content()),
    })
    @GetMapping("/joke/{id}")
    public Joke getJoke(@PathVariable("id") Long id) {
        return jokeService.getJokeById(id).orElseThrow(() -> new JokeNotFoundException(id));
    }

    @Operation(summary = "Get today's joke.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found today's joke.", content = {
                    @Content(schema = @Schema(implementation = Joke.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Joke not set for today.", content = @Content()),
    })
    @GetMapping("/joke/today")
    public Joke getJoke() {
        LocalDate today = LocalDate.now();
        return jokeService.getJokeByDate(today).orElseThrow(() -> new JokeNotFoundException(today));
    }

    @Operation(summary = "Add a new Joke.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added the joke", content = {
                    @Content(schema = @Schema(implementation = Joke.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "409", description = "Conflict with an existing joke.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid joke provided.", content = @Content())
    })
    @PostMapping("/joke/add")
    public Joke addJoke(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Joke to add", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Joke.class),
                    examples = @ExampleObject(value = "{ \"joke\": \"Funny Joke\", \"date\": \"2025-02-24\", \"description\": \"Explained jokes are the funniest.\" }")))
                            @RequestBody @Valid Joke joke) {
        return jokeService.addJoke(joke);
    }

    @Operation(summary = "Update an existing joke.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the joke", content = {
                    @Content(schema = @Schema(implementation = Joke.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "409", description = "Conflict with an existing joke.", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Invalid joke provided.", content = @Content()),
            @ApiResponse(responseCode = "404", description = "Existing joke not found to update.", content = @Content())
    })
    @PutMapping("/joke/update")
    public Joke updateJoke(@RequestBody @Valid Joke joke) {
        return jokeService.updateJoke(joke);
    }

    @Operation(summary = "Delete a joke by id if it exists.")
    @DeleteMapping("/joke/delete/{id}")
    public void deleteJoke(@PathVariable("id") Long id) {
        jokeService.deleteJoke(id);
    }
}
