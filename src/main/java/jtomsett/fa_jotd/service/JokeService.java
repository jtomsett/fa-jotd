package jtomsett.fa_jotd.service;

import jtomsett.fa_jotd.dao.Joke;

import java.time.LocalDate;
import java.util.Optional;

public interface JokeService {

    Joke addJoke(Joke joke);
    Joke updateJoke(Joke joke);

    Optional<Joke> getJokeById(Long id);
    Optional<Joke> getJokeByDate(LocalDate date);

    void deleteJoke(Long id);

}

