package at.spengergasse.vaadin.views.Animal;

import at.spengergasse.vaadin.domain.Animal;
import at.spengergasse.vaadin.service.AnimalService;
import at.spengergasse.vaadin.views.MainLayout;
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
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Route(value = "animal-edit/:animalId", layout = MainLayout.class)
@PageTitle("Tier bearbeiten")


public class AnimalEditView extends VerticalLayout implements BeforeEnterObserver
{

    private final TextField name = new TextField("Name");
    private final TextField color = new TextField("Color");
    private final Select<Character> type = new Select<>();
    private final NumberField price = new NumberField("Price");


    private final Button saveButton = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen",  e -> UI.getCurrent().navigate(AnimalListView.class));

    private final BeanValidationBinder<Animal> binder = new BeanValidationBinder<>(Animal.class);
    private final AnimalService animalService;

    private Animal animal = new Animal();

    public AnimalEditView(AnimalService animalService)
    {
        this.animalService = animalService;
        init();
    }

    private void init(){
        FormLayout formLayout = new FormLayout();
        type.setItems('1', '2', '3', 'a', 'b', 'c');

        formLayout.add(name, color, type, price);

        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addClickListener(e -> onSave());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        add(formLayout, buttonLayout);
        binder.bindInstanceFields(this);
    }

    private void onSave()
    {
        try{
            binder.writeBean(animal);
            animalService.save(animal);
            Notification.show("Tier erfolgreich gespeichert", 3000, Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate(AnimalListView.class);
        } catch(Exception e){
            log.error("Fehler beim Speichern", e.getMessage());
            Notification.show("Fehler beim Speichern" + e.getMessage());
        }
    }

    private void setAnimal(Animal animal){
        this.animal = animal;
        binder.readBean(animal);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event){
        Optional<String> paramOptional = event.getRouteParameters().get(AnimalListView.PARAM_ANIMAL_ID);

        paramOptional.ifPresent(new Consumer<String>()
        {
            @Override
            public void accept(String animalIdString)
            {
                try{
                    Long animalId = Long.parseLong(animalIdString);

                    Optional<Animal> animalOptional = animalService.findById(animalId);

                    if(animalOptional.isPresent()){
                        Animal animal = animalOptional.get();
                        binder.setBean(animal);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Fehler beim Speichern");
                }

            }
        });
    }
}
