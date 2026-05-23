import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PrimeiroTesteApiComConf {

    @BeforeClass
    public static void setup() {
        // API Brasileira gratuita voltada para estudos de QA
        RestAssured.baseURI = "https://serverest.dev";
    }

    @AfterClass
    public static void tearDown() {
        // Reseta o URI base e outras configurações globais para os valores padrão
        RestAssured.reset();
    }

    @Test
    public void deveListarUsuariosComSucessoSemRestAssuredResetBaseuri() {

        // 1. Configura e executa a requisição
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .get("/usuarios");

        // 2. Imprime o corpo da resposta no console
        response.then().log().all();

        // 3. Executa as validações com JUnit 4
        assertEquals(200, response.getStatusCode());

        int quantidade = response.path("quantidade");
        assertTrue("A quantidade deve ser maior ou igual a zero", quantidade >= 0);

        assertNotNull("A lista de usuários não deve ser nula", response.path("usuarios"));

        //Valida se a lista esta vazia
        List<Object> usuarios = response.path("usuarios");
        assertTrue("A lista de usuários está vazia", usuarios.size() > 0);

    }

    @Test
    public void deveCadastrarUsuarioComSucesso() {
        // 1. Criação dos dados dinâmicos
        String emailDinamico = "usuario" + System.currentTimeMillis() + "@qa.com";
        String novoUsuario = "{\n" +
                "  \"nome\": \"Fulano da Silva\",\n" +
                "  \"email\": \"" + emailDinamico + "\",\n" +
                "  \"password\": \"teste123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        // 2. Configura e executa a requisição POST
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .post("/usuarios");

        // 3. Imprime o log completo da resposta no console
        response.then().log().all();

        // 4. Executa as validações com JUnit 4
        assertEquals(201, response.getStatusCode());
        assertEquals("Cadastro realizado com sucesso", response.path("message"));
        assertNotNull("O campo _id não deve ser nulo", response.path("_id"));
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() {
        // 1. Criação dos dados dinâmicos para atualização
        String usuarioAtualizado = "{\n" +
                "  \"nome\": \"Fulano Alterado\",\n" +
                "  \"email\": \"alterado" + System.currentTimeMillis() + "@qa.com\",\n" +
                "  \"password\": \"novaSenha123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        // 2. Configura e executa a requisição PUT
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .put("/usuarios/0uxu6GwOfwnt6aMc");

        // 3. Imprime o log completo da resposta de forma válida
        response.then().log().all();

        // 4. Executa as validações com JUnit 4 (Substituição do anyOf)
        int statusCode = response.getStatusCode();
        assertTrue("O status code deve ser 200 ou 201", statusCode == 200 || statusCode == 201);

        String mensagem = response.path("message");
        assertTrue("A mensagem deve ser de alteração ou de cadastro com sucesso",
                "Registro alterado com sucesso".equals(mensagem) || "Cadastro realizado com sucesso".equals(mensagem));
    }

    @Test
    public void deveDeletarUsuarioComSucesso() {
        // 1. Configura e executa a requisição DELETE
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .delete("/usuarios/idInexistente123");

        // 2. Imprime o log completo da resposta no console
        response.then().log().all();

        // 3. Executa as validações com JUnit 4
        assertEquals(200, response.getStatusCode());
//        assertEquals("Nenhum registro excluído", response.path("message"));//
//        assertEquals("Registro excluído com sucesso", response.path("message"));//
    }
}
