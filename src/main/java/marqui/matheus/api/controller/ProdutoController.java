package marqui.matheus.api.controller;

import marqui.matheus.domain.entity.Cliente;
import marqui.matheus.domain.entity.Produto;
import marqui.matheus.domain.repository.Produtos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private Produtos produtos;
    private static Integer PER_PAGE = 100;

    @Autowired
    public ProdutoController(Produtos produtos) {
        this.produtos = produtos;
    }

    @GetMapping({"/paginated/{page}/{perPage}", "/paginated/{page}"})
    public Iterable<Produto> getAllProducts(@PathVariable Integer page,
                                            @PathVariable(required = false) Optional<Integer> perPage) {
        Integer maxPages = PER_PAGE;
        if(perPage.isPresent() && perPage.get() < maxPages){
            maxPages = perPage.get();
        }
        Pageable pageable = PageRequest.of(page, maxPages);
        return produtos.findAll(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto createProduct(@RequestBody @Valid Produto produto){
        return produtos.save(produto);
    }

    @GetMapping("/{sku}")
    public Produto getBySku(@PathVariable String sku) {
        return produtos
                .findBySkuIgnoreCase(sku)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Produto getById(@RequestParam("id") Integer id) {
        return produtos
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{sku}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String sku) {
        Produto produto = this.getBySku(sku);
        if(produto == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            else produtos.delete(produto);
    }

    @PutMapping("/{sku}")
    public Produto update(@PathVariable String sku,
                          @RequestBody @Valid Produto produto){
        Produto savedEditingProduct = getBySku(sku);
        if (savedEditingProduct == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        else{
            produto.setId(savedEditingProduct.getId());
            return produtos.save(produto);
        }
    }

    @GetMapping("/search")
    public List<Produto> find(Produto filtro) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(
                        ExampleMatcher.StringMatcher.CONTAINING);

        Example<Produto> example =  Example.of(filtro, matcher);

        return produtos.findAll(example);
    }


}
