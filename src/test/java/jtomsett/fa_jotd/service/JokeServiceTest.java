package jtomsett.fa_jotd.service;

import jakarta.validation.ValidationException;
import jtomsett.fa_jotd.dao.Joke;
import jtomsett.fa_jotd.repository.JokeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JokeServiceTest {

    @Mock
    private JokeRepository jokeRepository;

    @InjectMocks
    private JokeServiceImpl jokeService;

    // addJoke() tests
    @Test
    void addJokeValidTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        Joke savedJoke = new Joke(incomingJoke.getJoke(), incomingJoke.getDate());
        savedJoke.setId(12L);
        when(jokeRepository.save(incomingJoke)).thenReturn(savedJoke);

        Joke returnedJoke = jokeService.addJoke(incomingJoke);

        assertEquals(returnedJoke,savedJoke);
        assertNotNull(returnedJoke.getId());
    }

    @Test
    void addJokeNullTest(){
        assertThrows(ValidationException.class, () -> jokeService.addJoke(null));
    }

    @Test
    void addJokeInvalidDateTest(){
        when(jokeRepository.existsJokeByDate(Mockito.any())).thenReturn(true);
        assertThrows(ValidationException.class, () -> jokeService.addJoke(new Joke("Joke", LocalDate.now())));
    }

    //updateJoke() Tests
    @Test
    void updateJokeValidTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        incomingJoke.setId(12L);
        Joke savedJoke = new Joke("old joke", incomingJoke.getDate());
        savedJoke.setId(12L);

        when(jokeRepository.findById(incomingJoke.getId())).thenReturn(Optional.of(savedJoke));
        when(jokeRepository.save(incomingJoke)).thenReturn(incomingJoke);

        Joke returnedJoke = jokeService.updateJoke(incomingJoke);
        assertEquals(returnedJoke,incomingJoke);
        assertEquals(returnedJoke.getId(),incomingJoke.getId());
    }

    @Test
    void updateJokeDateChangeTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        incomingJoke.setId(12L);
        Joke savedJoke = new Joke(incomingJoke.getJoke(), incomingJoke.getDate().plusDays(2));
        savedJoke.setId(12L);

        when(jokeRepository.findById(incomingJoke.getId())).thenReturn(Optional.of(savedJoke));
        when(jokeRepository.findByDate(Mockito.any())).thenReturn(Optional.empty());
        when(jokeRepository.save(incomingJoke)).thenReturn(incomingJoke);

        Joke returnedJoke = jokeService.updateJoke(incomingJoke);
        assertEquals(returnedJoke,incomingJoke);
        assertEquals(returnedJoke.getId(),incomingJoke.getId());
    }

    @Test
    void updateJokeExistingJokeOnDateTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        incomingJoke.setId(12L);
        Joke savedJoke = new Joke(incomingJoke.getJoke(), incomingJoke.getDate().plusDays(2));
        savedJoke.setId(12L);
        Joke savedJoke2 = new Joke(incomingJoke.getJoke(), incomingJoke.getDate());
        savedJoke2.setId(15L);

        when(jokeRepository.findById(incomingJoke.getId())).thenReturn(Optional.of(savedJoke));
        when(jokeRepository.findByDate(Mockito.any())).thenReturn(Optional.of(savedJoke2));

        assertThrows(ValidationException.class, () -> jokeService.updateJoke(incomingJoke));
    }

    @Test
    void updateJokeDoesNotExistTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        incomingJoke.setId(15L);
        Joke savedJoke = new Joke(incomingJoke.getJoke(), incomingJoke.getDate());
        savedJoke.setId(12L);
        when(jokeRepository.findById(incomingJoke.getId())).thenReturn(Optional.empty());
        when(jokeRepository.save(incomingJoke)).thenReturn(savedJoke);

        Joke returnedJoke = jokeService.updateJoke(incomingJoke);

        assertEquals(returnedJoke,savedJoke);
        assertNotNull(returnedJoke.getId());
    }

    @Test
    void updateJokeNullIdTest(){
        Joke incomingJoke = new Joke("Joke", LocalDate.now());
        Joke savedJoke = new Joke(incomingJoke.getJoke(), incomingJoke.getDate());
        savedJoke.setId(12L);
        when(jokeRepository.save(incomingJoke)).thenReturn(savedJoke);

        Joke returnedJoke = jokeService.updateJoke(incomingJoke);

        assertEquals(returnedJoke,savedJoke);
        assertNotNull(returnedJoke.getId());
    }

    @Test
    void updateJokeNullJokeTest(){
        assertThrows(ValidationException.class, () -> jokeService.updateJoke(null));
    }


    //getJokeById tests
    @Test
    void getJokeByIdValidTest(){
        Joke expectedJoke = new Joke("Joke", LocalDate.now());
        expectedJoke.setId(12L);

        when(jokeRepository.findById(Mockito.eq(12L))).thenReturn(Optional.of(expectedJoke));
        Optional<Joke> returnedJoke =  jokeService.getJokeById(12L);
        assertTrue(returnedJoke.isPresent());
        assertEquals(returnedJoke.get(),expectedJoke);
    }

    @Test void getJokeByIdNullIdTest(){
        Optional<Joke> returnedJoke =  jokeService.getJokeById(null);
        assertFalse(returnedJoke.isPresent());
    }

    //getJokeByDate(LocalDate date) test
    @Test
    void getJokeByDateValidTest(){
        Joke expectedJoke = new Joke("Joke", LocalDate.now());
        expectedJoke.setId(12L);

        when(jokeRepository.findByDate(Mockito.eq(expectedJoke.getDate()))).thenReturn(Optional.of(expectedJoke));
        Optional<Joke> returnedJoke =  jokeService.getJokeByDate(expectedJoke.getDate());
        assertTrue(returnedJoke.isPresent());
        assertEquals(returnedJoke.get(),expectedJoke);
    }

    @Test void getJokeByDateNullDateTest(){
        Optional<Joke> returnedJoke =  jokeService.getJokeByDate(null);
        assertFalse(returnedJoke.isPresent());
    }

    //deleteJoke(Long id) tests
    @Test
    void deleteJokeByIdValidTest(){
        jokeService.deleteJoke(12L);
        verify(jokeRepository,times(1)).deleteById(Mockito.isNotNull());
    }

    @Test
    void deleteJokeByIdNullIdTest(){
        jokeService.deleteJoke(null);
        verify(jokeRepository,times(0)).deleteById(Mockito.isNotNull());
    }
}
