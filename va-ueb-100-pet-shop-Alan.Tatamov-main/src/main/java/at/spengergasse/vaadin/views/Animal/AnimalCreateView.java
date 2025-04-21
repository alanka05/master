package at.spengergasse.vaadin.views.Animal;

import at.spengergasse.vaadin.domain.Animal;
import at.spengergasse.vaadin.service.AnimalService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.router.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIcon;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.Optional;
import java.util.function.Consumer;


@Slf4j
@Route(value = "animal-form/:ANIMAL_ID?", layout = MainLayout.class)
@PageTitle("Tier Formular")
@Menu(title = "Tier erstellen", icon = LineAwesomeIconUrl.ACCUSOFT)


public class AnimalCreateView extends VerticalLayout implements BeforeEnterObserver
{
    private final TextField name = new TextField("Name");
    private final TextField color = new TextField();
    private final Select<Character> type = new Select<>();
    private final NumberField price = new NumberField();

    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Abbrechen", LineAwesomeIcon.ADDRESS_BOOK.create(), new ComponentEventListener<ClickEvent<Button>>() {
        @Override
        public void onComponentEvent(ClickEvent<Button> e) {
            onCancel();
        }
    });

    private final BeanValidationBinder<Animal> binder = new BeanValidationBinder<>(Animal.class);
    private final AnimalService animalService;

    private Animal animal = new Animal();

    public AnimalCreateView(@Autowired AnimalService animalService)
    {
        this.animalService = animalService;
        init();
    }

    private void init()
    {
        FormLayout formLayout = new FormLayout();
        name.setLabel("Name");
        color.setLabel("Color");
        type.setLabel("Type");
        type.setItems('1', '2', '3', 'a', 'b', 'c');
        price.setLabel("Price");

        formLayout.add(name, color, type, price);


        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("padding", "1rem");
        saveButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>()
        {
            @Override
            public void onComponentEvent(ClickEvent<Button> event)
            {
                String wert = "haha";
                onSave();
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        add(formLayout);
        add(buttonLayout);

        binder.bindInstanceFields(this);
    }

    private void onSave()
    {
        try{
            binder.writeBean(animal);
            log.debug("Animal unsaved = {}", animal);
            animalService.save(animal);
            log.debug("Animal saved = {}", animalService.findById(animal.getId()));

            Notification.show("Daten wurden gespeichert");

            Optional<UI> uiOptional = getUI();
            uiOptional.ifPresent(new Consumer<UI>()
            {
                @Override
                public void accept(UI ui)
                {
                    ui.navigate(AnimalListView.class);
                }
            });

        }   catch (IllegalArgumentException e){
            log.error("Ungültige Eingabe beim Speichern des Tiers: {}", e.getMessage());
            Notification.show("Ungültige Eingabe: " + e.getMessage());
        }   catch (Exception e){
            log.error("Fehler beim Speichern des Tiers={}", e.getMessage());
            Notification.show("Ein unerwarteter Fehler ist aufgetreten beim Speichern." + e.getMessage());
        }
    }

    private void onCancel()
    {
        UI.getCurrent().navigate(AnimalListView.class);
    }

    private void setAnimal(Animal animal){
        this.animal = animal;
        binder.readBean(animal);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        event.getRouteParameters().get("ANIMAL_ID").ifPresent(new Consumer<String>()
        {
            @Override
            public void accept(String animalId)
            {
                animalService.findById( Long.parseLong(animalId)).ifPresent(new Consumer<Animal>()
                {
                    @Override
                    public void accept(Animal animal)
                    {
                        setAnimal(animal);
                    }
                });
            }
        });
    }
}
