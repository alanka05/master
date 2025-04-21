package at.spengergasse.vaadin.service;

import at.spengergasse.vaadin.core.service.AbstractService;
import at.spengergasse.vaadin.domain.Animal;
import org.springframework.stereotype.Service;

@Service
public class AnimalService extends AbstractService<Animal> {

    public AnimalService() {
        super(50, (idSupplier, faker) -> new Animal(idSupplier.get(),
                faker.superhero().name(),
                faker.color().name(),
                faker.lorem().character(),
                (double)faker.random().nextInt(1000, 8000)
        ));
    }
}
