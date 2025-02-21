package jtomsett.fa_jotd.exceptions;

import java.time.LocalDate;

public class JokeNotFoundException extends RuntimeException {

    public JokeNotFoundException(Long id) {
        super("Could not find joke by id:" + id);
    }

    public JokeNotFoundException(LocalDate date) {
        super("Could not find joke by date:" + date);
    }

}
