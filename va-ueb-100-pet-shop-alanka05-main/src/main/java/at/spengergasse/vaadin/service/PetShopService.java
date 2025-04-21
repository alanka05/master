package at.spengergasse.vaadin.service;

import at.spengergasse.vaadin.core.service.AbstractService;
import at.spengergasse.vaadin.domain.PetShop;
import org.springframework.stereotype.Service;

@Service
public class PetShopService extends AbstractService<PetShop> {

    public PetShopService() {
        super(100, (idSupplier, faker) -> new PetShop(idSupplier.get(),
                faker.company().name(),
                faker.address().fullAddress()));
    }
}
