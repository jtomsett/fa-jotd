package jtomsett.fa_jotd.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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



    @Operation(summary = "Find a joke by id or date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found the joke", content = {
                    @Content(schema = @Schema(implementation = Joke.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "404", description = "Joke not found for provided id.", content = @Content()),
    })
    @GetMapping("/joke")
    public Joke getJoke(
            @Parameter(name = "id", description = "Highest precedent if available.", example = "12")
            @RequestParam(required = false) Long id,
            @Parameter(name = "date", description = "Lower precedent than id, defaults to today's date if neither are present.", example = "2025-02-24")
            @RequestParam(required = false) LocalDate date) {
        if (id != null){
            return jokeService.getJokeById(id).orElseThrow(() -> new JokeNotFoundException(id));
        }

        if(date != null){
            return jokeService.getJokeByDate(date).orElseThrow(() -> new JokeNotFoundException(date));
        }

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
