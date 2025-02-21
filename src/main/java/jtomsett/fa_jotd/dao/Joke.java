package jtomsett.fa_jotd.dao;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;


@Entity
@Data
@NoArgsConstructor
public class Joke {

    public Joke(String joke, LocalDate date) {
        this.joke = joke;
        this.date = date;
    }

    @Id
    private @GeneratedValue Long id;

    @NotBlank(message = "Joke is required.")
    private String joke;

    @NotNull(message = "Date is required.")
    private LocalDate date;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Joke joke1 = (Joke) o;
        return Objects.equals(joke, joke1.joke) && Objects.equals(date, joke1.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(joke, date);
    }
}
