package at.spengergasse.vaadin.domain;

import at.spengergasse.vaadin.core.domain.AbstractEntity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Animal extends AbstractEntity {

    @NotBlank(message = "Name darf nicht leer sein")
    private String name;
    @NotBlank(message = "Farbe darf nicht leer sein")
    private String color;
    @NotNull(message = "Typ darf nicht leer sein")
    private char type;
    @DecimalMin(value= "0.0", inclusive = false, message = "Preis muss positiv sein")
    private double price;

    public Animal(Long id, String name, String color, char type, double price) {
        setId(id);
        setName(name);
        setColor(color);
        setType(type);
        setPrice(price);
    }

    public Animal()
    {

    }
}
