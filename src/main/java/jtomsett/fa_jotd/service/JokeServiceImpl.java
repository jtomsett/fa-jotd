package jtomsett.fa_jotd.service;

import jakarta.validation.ValidationException;
import jtomsett.fa_jotd.dao.Joke;
import jtomsett.fa_jotd.exceptions.InvalidJokeException;
import jtomsett.fa_jotd.exceptions.JokeExistsForDateException;
import jtomsett.fa_jotd.exceptions.JokeNotFoundException;
import jtomsett.fa_jotd.repository.JokeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JokeServiceImpl implements JokeService {

    private final JokeRepository jokeRepository;


    @Override
    public Joke addJoke(Joke joke) throws JokeExistsForDateException {

        if (joke == null) {
            throw new ValidationException("Cannot add a null joke.");
        }

        if (joke.getDate() != null && jokeRepository.existsJokeByDate(joke.getDate())) {
            throw new JokeExistsForDateException(joke.getDate());
        }

        return jokeRepository.save(joke);
    }

    @Override
    public Joke updateJoke(Joke joke) throws InvalidJokeException, JokeNotFoundException, JokeExistsForDateException {
        if (joke == null) {
            throw new InvalidJokeException("Cannot update a null joke.");
        }

        if (joke.getId() == null) {
            throw new InvalidJokeException("Cannot update a joke without an id.");
        }

        Optional<Joke> oJoke = getJokeById(joke.getId());
        if (oJoke.isEmpty()) {
            throw new JokeNotFoundException(joke.getId());
        }

        if(joke.getDate() != null &&
                oJoke.get().getDate() != null &&
                !oJoke.get().getDate().equals(joke.getDate())){

            Optional<Joke> oJokeByDate = getJokeByDate(joke.getDate());

            if(oJokeByDate.isPresent() &&
                    oJokeByDate.get().getId() != null &&
                    oJoke.get().getId() != null &&
                    !oJokeByDate.get().getId().equals(oJoke.get().getId())){
                throw new JokeExistsForDateException("Another Joke already exists for specified date: "+joke.getDate());
            }
        }

        return jokeRepository.save(joke);
    }

    @Override
    public Optional<Joke> getJokeById(Long id) {
        if(id == null) {
            return Optional.empty();
        }
        return jokeRepository.findById(id);
    }

    @Override
    public Optional<Joke> getJokeByDate(LocalDate date) {
        if (date == null) {
            return Optional.empty();
        }
        return jokeRepository.findByDate(date);
    }

    @Override
    public void deleteJoke(Long id) {
        if (id != null) {
            jokeRepository.deleteById(id);
        }
    }
}
