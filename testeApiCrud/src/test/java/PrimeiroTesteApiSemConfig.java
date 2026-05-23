import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class PrimeiroTesteApiSemConfig {

    @Test
    public void deveListarUsuariosComSucesso() {
        RestAssured.baseURI = "https://serverest.dev";
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .get("/usuarios");
        response.peek();

        assertEquals(200, response.getStatusCode());
        int quantidade = response.path("quantidade");
        assertTrue("A quantidade deve ser maior ou igual a zero",
                quantidade >= 0);
        assertNotNull("A lista de usuários não deve ser nula",
                response.path("usuarios"));
        RestAssured.reset();
    }

    @Test
    public void deveCadastrarUsuarioComSucesso() {
        RestAssured.baseURI = "https://serverest.dev";
        String emailDinamico = "usuario" + System.currentTimeMillis() + "@qa.com";
        String novoUsuario = "{\n" +
                "  \"nome\": \"Fulano da Silva\",\n" +
                "  \"email\": \"" + emailDinamico + "\",\n" +
                "  \"password\": \"teste123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .body(novoUsuario)
                .post("/usuarios");
        response.then().log().all();
        assertEquals(201, response.getStatusCode());
        assertEquals("Cadastro realizado com sucesso", response.path("message"));
        assertNotNull("O campo _id não deve ser nulo", response.path("_id"));

        //Valida se a lista esta vazia
        List<Object> usuarios = response.path("usuarios");
        assertTrue("A lista de usuários está vazia", usuarios.size() > 0);
        RestAssured.reset();
    }

    @Test
    public void deveAtualizarUsuarioComSucesso() {
        RestAssured.baseURI = "https://serverest.dev";
        String usuarioAtualizado = "{\n" +
                "  \"nome\": \"Fulano Alterado\",\n" +
                "  \"email\": \"alterado" + System.currentTimeMillis() + "@qa.com\",\n" +
                "  \"password\": \"novaSenha123\",\n" +
                "  \"administrador\": \"true\"\n" +
                "}";

        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .body(usuarioAtualizado)
                .put("/usuarios/0uxu6GwOfwnt6aMc");
        response.then().log().all();
        int statusCode = response.getStatusCode();
        assertTrue("O status code deve ser 200 ou 201", statusCode == 200 || statusCode == 201);
        String mensagem = response.path("message");
        assertTrue("A mensagem deve ser de alteração ou de cadastro com sucesso",
                "Registro alterado com sucesso".equals(mensagem) || "Cadastro realizado com sucesso".equals(mensagem));
        RestAssured.reset();
    }

    @Test
    public void deveDeletarUsuarioComSucesso() {
        RestAssured.baseURI = "https://serverest.dev";
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .delete("/usuarios/idInexistente123");
        response.then().log().all();
        assertEquals(200, response.getStatusCode());
//        assertEquals("Nenhum registro excluído", response.path("message"));//
//        assertEquals("Registro excluído com sucesso", response.path("message"));//
        RestAssured.reset();
    }
}
