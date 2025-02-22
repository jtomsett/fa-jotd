package jtomsett.fa_jotd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jtomsett.fa_jotd.controller.JokeRestController;
import jtomsett.fa_jotd.dao.Joke;
import jtomsett.fa_jotd.service.JokeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class FaJotdApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private JokeRestController jokeRestController;

	@Autowired
	private JokeService jokeService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@AfterEach
	void afterEach() throws DataAccessException {
		JdbcTestUtils.deleteFromTables(jdbcTemplate, "joke");
	}

	@Test
	void contextLoads() {
		assertThat(jokeRestController).isNotNull();
		assertThat(jokeService).isNotNull();
	}

	@Test
	void addJoke_valid() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").value("Funny Joke Description"));
	}

	@Test
	void addJoke_valid_noDescription() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").isEmpty());
	}

	@Test
	void addJoke_invalid_missingDate() throws Exception {
		String requestBody = "{ \"joke\": \"Funny Joke\", \"description\": \"Funny Joke Description\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void addJoke_invalid_missingJoke() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void addJoke_invalid_jokeForDateExists() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").value("Funny Joke Description"));

		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().is4xxClientError());
	}

	@Test
	void getTodaysJoke_Valid() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").value("Funny Joke Description"));

		this.mvc.perform(get("/joke/today"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").value("Funny Joke Description"));
	}

	@Test
	void getTodaysJoke_NotAvailable() throws Exception {
		this.mvc.perform(get("/joke/today"))
				.andExpect(status().isNotFound());
	}

	@Test
	void getJokeById_Found() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		MvcResult result = this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = JsonMapper.builder()
				.findAndAddModules()
				.build();

		Joke savedJoke = mapper.readValue(result.getResponse().getContentAsString(), Joke.class);

		this.mvc.perform(get("/joke/"+savedJoke.getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id").value(savedJoke.getId()))
				.andExpect(jsonPath("$.joke").value("Funny Joke"))
				.andExpect(jsonPath("$.date").value(now.toString()))
				.andExpect(jsonPath("$.description").value("Funny Joke Description"));

	}

	@Test
	void getJokeById_NotFound() throws Exception {
		this.mvc.perform(get("/joke/13"))
				.andExpect(status().isNotFound());
	}

	@Test
	void deleteJoke_Exists() throws Exception {
		LocalDate now = LocalDate.now();
		String requestBody = "{ \"joke\": \"Funny Joke\", \"date\": \""+now+"\", \"description\": \"Funny Joke Description\"}";
		MvcResult result = this.mvc.perform(post("/joke/add")
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		ObjectMapper mapper = JsonMapper.builder()
				.findAndAddModules()
				.build();

		Joke savedJoke = mapper.readValue(result.getResponse().getContentAsString(), Joke.class);

		this.mvc.perform(delete("/joke/delete/"+savedJoke.getId()))
				.andExpect(status().isOk());

	}

	@Test
	void deleteJoke_NotFound() throws Exception {
		this.mvc.perform(delete("/joke/delete/13"))
				.andExpect(status().isNotFound());
	}

	//TODO joke update tests

}
