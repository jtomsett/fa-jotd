package jtomsett.fa_jotd.repository;

import jtomsett.fa_jotd.dao.Joke;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface JokeRepository extends CrudRepository<Joke, Long> {

    Optional<Joke> findByDate(LocalDate date);
    boolean existsJokeByDate(LocalDate date);
}
