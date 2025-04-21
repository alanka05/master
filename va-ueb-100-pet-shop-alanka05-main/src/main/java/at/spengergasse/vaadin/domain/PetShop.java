package at.spengergasse.vaadin.domain;

import at.spengergasse.vaadin.core.domain.AbstractEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PetShop extends AbstractEntity {

    @NotBlank(message = "Name darf nicht leer sein")
    private String name;
    @NotBlank(message = "Adresse darf nicht leer sein")
    private String address;

    public PetShop(Long id, String name, String address) {
        setId(id);
        setName(name);
        setAddress(address);
    }

    public PetShop()
    {

    }
}
