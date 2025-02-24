package jtomsett.fa_jotd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class JokeExistsForDateException extends RuntimeException {
    public JokeExistsForDateException(LocalDate date) {super("Joke already exists for specified date: "+date);}
    public JokeExistsForDateException(String message) {super(message);}
}
