package fi.homebrewing.competition.restcontroller;

import java.util.Optional;
import java.util.UUID;

import fi.homebrewing.competition.domain.Beer;
import fi.homebrewing.competition.domain.BeerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/beers")
public class BeerController {
    private final BeerRepository beerRepository;

    public BeerController(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @GetMapping("/")
    Iterable<Beer> getBeers() {
        return beerRepository.findAll();
    }

    @GetMapping("/{id}")
    Optional<Beer> getBeerById(@PathVariable String id) {
        return beerRepository.findById(id);
    }

    @PostMapping("/")
    Beer postBeer(@RequestBody Beer beer) {
        return beerRepository.save(beer);
    }

    @PutMapping("/{id}")
    ResponseEntity<Beer> putBeer(@PathVariable String id, @RequestBody Beer beer) {
        return beerRepository.existsById(id)
            ? new ResponseEntity<>(beerRepository.save(beer), HttpStatus.OK)
            : new ResponseEntity<>(beerRepository.save(beer), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    void deleteBeer(@PathVariable String id) {
        beerRepository.deleteById(id);
    }
}
