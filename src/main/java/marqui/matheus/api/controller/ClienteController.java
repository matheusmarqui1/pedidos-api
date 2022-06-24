package marqui.matheus.api.controller;

import io.swagger.annotations.*;
import marqui.matheus.domain.entity.Cliente;
import marqui.matheus.domain.entity.Pedido;
import marqui.matheus.domain.repository.Clientes;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/clientes")
@Api("Api Clientes")
public class ClienteController {

    private Clientes clientes;

    public ClienteController(Clientes clientes) {
        this.clientes = clientes;
    }

    @GetMapping("/{id}")
    @ApiOperation("Obter detalhes de um cliente")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente encontrado."),
            @ApiResponse(code = 404, message = "Cliente não encontrado para " +
                    "o id informado.")
    })
    public Cliente getClienteById(
            @PathVariable
            @ApiParam("Id do cliente") Integer id) {
        return clientes
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente não encontrado."
                ));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Salvar um cliente.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Cliente salvo com sucesso."),
            @ApiResponse(code = 400, message = "Erro de validação.")
    })
    public Cliente save(@RequestBody @Valid Cliente cliente){
        return clientes.save(cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Deletar um cliente.")
    @ApiResponses({
            @ApiResponse(code = 204, message = "Cliente excluído com sucesso."),
            @ApiResponse(code = 404, message = "Cliente não encontrado para " +
                    "o id informado.")
    })
    public void delete(@PathVariable Integer id){
        clientes.findById(id)
                .map(cliente -> {
                    clientes.delete(cliente);
                    return cliente;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Não é possível deletar um cliente inexistente."));
    }

    @PutMapping("/{id}")
    @ApiOperation("Modificar um cliente.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cliente modificado com sucesso."),
            @ApiResponse(code = 404, message = "Cliente não encontrado para " +
                    "o id informado.")
    })
    public Cliente update(@PathVariable Integer id,
                          @RequestBody @Valid Cliente cliente){
        return clientes
                .findById(id)
                .map(clienteExistente -> {
                    cliente.setId(clienteExistente.getId());
                    clientes.save(cliente);
                    return cliente;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    @ApiOperation("Buscar clientes com base em um objeto filtro.")
    @ApiResponse(code = 200, message = "Filtro aplicado à lista de resultado, entretanto " +
            "pode estar vazia caso não hajam clientes que se encaixem ao filtro.")
    public List<Cliente> find(Cliente filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching().withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Example<Cliente> example =  Example.of(filtro, matcher);

        return clientes.findAll(example);
    }

    @GetMapping("{id}/pedidos")
    @ApiOperation("Buscar os pedidos de um cliente.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Pedidos do cliente carregados com sucesso."),
            @ApiResponse(code = 404, message = "Cliente não encontrado para " +
                    "o id informado.")
    })
    public Set<Pedido> findPedidos(@PathVariable Integer id) {
        return clientes.findById(id)
                .map(cliente -> cliente.getPedidos())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Não é possível buscar os pedidos de um cliente inexistente."));
    }
}
