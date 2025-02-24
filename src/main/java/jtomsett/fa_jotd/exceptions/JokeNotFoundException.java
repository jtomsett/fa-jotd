package jtomsett.fa_jotd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class JokeNotFoundException extends RuntimeException {

    public JokeNotFoundException(Long id) {
        super("Could not find joke by id: " + id);
    }

    public JokeNotFoundException(LocalDate date) {
        super("Could not find joke by date: " + date);
    }

}
