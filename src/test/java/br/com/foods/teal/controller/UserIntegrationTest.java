package br.com.foods.teal.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Classe para teste de integração do usuário
 * 
 * @author Caio Pereira Leal 
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("teste")
public class UserIntegrationTest {

	@Autowired
    private MockMvc mockMvc;

	/**
	 * Classe incia antes dos testes e deixa um usúario registrado
	 * 
	 * @throws Exception
	 */
    @BeforeEach
    void setUp() throws Exception {
        String json = """
            {
                "id": 1,
                "name": "Teste",
                "email": "teste@email.com",
                "phone": "123456789",
                "cpf": "12345678900"
            }
        """;

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }
    
    /**
     * Testa a funcionalidade de buscar todos os registro dos usuários
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/user")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Teste")))
                .andExpect(jsonPath("$[0].email", is("teste@email.com")))
                .andExpect(jsonPath("$[0].phone", is("123456789")))
                .andExpect(jsonPath("$[0].cpf", is("12345678900")));
    }
    
    /**
     * Testa a funcionalidade de buscar um registro de usuário expecifico
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/user/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Teste"))
                .andExpect(jsonPath("$.email").value("teste@email.com"))
                .andExpect(jsonPath("$.phone").value("123456789"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    /**
     * Testa a funcionalidade de criar um registro de um novo usuário
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testCreateUser() throws Exception {
        String json = """
            {
                "id": 1,
                "name": "Teste",
                "email": "teste@email.com",
                "phone": "123456789",
                "cpf": "12345678900"
            }
        """;

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
    }
    
    /**
     * Testa a funcionalidade se ocorrer um erro ao criar um registro do usuário
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testCreateUserError() throws Exception {
        String json = """
            {
                "id": 1,
                "name": "",
                "email": "teste@email.com",
                "phone": "123456789",
                "cpf": "12345678900"
            }
        """;

        mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }
 
   
    /**
     * Testa a funcionalidade de atualizar o registro usuário
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testUpdateUser() throws Exception {
        String updatedJson = """
            {
                "id": 1,
                "name": "Updated Name",
                "email": "updated@email.com",
                "phone": "987654321",
                "cpf": "12345678900"
            }
        """;

        mockMvc.perform(put("/user/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.email").value("updated@email.com"))
                .andExpect(jsonPath("$.phone").value("987654321"))
                .andExpect(jsonPath("$.cpf").value("12345678900"));
    }

    /**
     * Testa a funcionalidade de deletar o registro do usuário
     * 
     * @throws Exception
     *              Lança Excelções
     */
    @Test
    void testDeleteUser() throws Exception {
        mockMvc.perform(delete("/user/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
