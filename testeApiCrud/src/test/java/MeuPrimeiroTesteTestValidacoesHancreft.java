import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;


public class MeuPrimeiroTesteTestValidacoesHancreft {

    @BeforeClass // 2. Adicione esta anotação para rodar antes de todos os testes
    public static void setup() {
        // API Brasileira gratuita voltada para estudos de QA
        RestAssured.baseURI = "https://serverest.dev";
    }

    @Test
    public void deveListarUsuariosComSucesso() {
        // 1. Configura e executa a requisição
        Response response = RestAssured
                .with()
                .contentType(ContentType.JSON)
                .get("/usuarios");

        // 2. Imprime o corpo da resposta no console
        response.peek();

        // 3. Executa as validações (Assertivas)
        response.then().statusCode(200);
        response.then().body("quantidade", greaterThanOrEqualTo(0));
        response.then().body("usuarios", notNullValue());
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


        // 4. Executa as validações (Assertivas)
        response.then().statusCode(201);
        response.then().body("message", equalTo("Cadastro realizado com sucesso"));
        response.then().body("_id", notNullValue());
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

        // 4. Executa as validações (Assertivas)
        response.then().statusCode(anyOf(equalTo(200), equalTo(201)));
        response.then().body("message", anyOf(equalTo("Registro alterado com sucesso"), equalTo("Cadastro realizado com sucesso")));
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

        // 3. Executa as validações (Assertivas)
        response.then().statusCode(200);
        response.then().body("message", equalTo("Nenhum registro excluído"));
    }
}
