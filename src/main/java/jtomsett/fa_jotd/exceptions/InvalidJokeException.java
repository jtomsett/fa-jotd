package jtomsett.fa_jotd.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidJokeException extends RuntimeException {

    public InvalidJokeException(String message) {super(message);}
}
