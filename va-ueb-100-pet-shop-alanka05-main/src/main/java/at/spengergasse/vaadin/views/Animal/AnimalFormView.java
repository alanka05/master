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

import java.util.Optional;
import java.util.function.Consumer;

@Slf4j
@Route(value = "animals/edit/:animalId?", layout = MainLayout.class)
@PageTitle("Tier erfassen / bearbeiten")
@Menu(title = "Tier erfassen", icon = "la la-paw")
public class AnimalFormView extends VerticalLayout implements BeforeEnterObserver
{

    private final TextField name = new TextField("Name");
    private final TextField color = new TextField("Farbe");
    private final Select<Character> type = new Select<>();
    private final NumberField price = new NumberField("Preis");

    private final Button saveButton = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen", LineAwesomeIcon.ADDRESS_CARD.create(), new ComponentEventListener<ClickEvent<Button>>()
    {
        @Override
        public void onComponentEvent(ClickEvent<Button> event)
        {
            onCancel();
        }
    });


    private final BeanValidationBinder<Animal> binder = new BeanValidationBinder<>(Animal.class);
    private final AnimalService animalService;

    private Animal animal = new Animal();

    public AnimalFormView(@Autowired AnimalService animalService)
    {
        this.animalService = animalService;
        init();
    }

    private void init()
    {
        FormLayout formLayout = new FormLayout();

        type.setItems('h', 'k', 'v', '1', '2');
        type.setLabel("Typ");
        name.setLabel("Name");
        color.setLabel("Farbe");
        price.setLabel("Preis");

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


        binder.setBean(new Animal());
    }

    private void onSave()
    {
        try
        {
            binder.writeBean(animal);
            log.debug("Animal unsaved : {}", animal);
            animalService.save(animal);
            log.debug("Animal saved : {}", animalService.findById(animal.getId()));

            Notification.show("Animal saved");

            Optional<UI> uiOptional = getUI();
            uiOptional.ifPresent(new Consumer<UI>()
            {

                @Override
                public void accept(UI ui)
                {
                    ui.navigate(AnimalListView.class);
                }
            });

        } catch (IllegalArgumentException e)
        {
            log.error(e.getMessage());
            Notification.show(e.getMessage());

        } catch (Exception e)
        {
            log.error("Fehler beim Speichern", e.getMessage());
            Notification.show("Fehler beim Speichern: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }

    private void onCancel()
    {
        UI.getCurrent().navigate(AnimalListView.class);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        Optional<String> param = event.getRouteParameters().get("animalId");

        param.ifPresent(animalIdStr ->
        {
            try
            {
                Long animalId = Long.parseLong(animalIdStr);
                animalService.findById(animalId).ifPresentOrElse(
                        animal -> binder.setBean(animal),
                        () ->
                        {
                            Notification.show("Tier nicht gefunden.");
                            UI.getCurrent().navigate(AnimalListView.class);
                        }
                );
            } catch (NumberFormatException e)
            {
                Notification.show("Ungültige Tier-ID.");
                UI.getCurrent().navigate(AnimalListView.class);
            }
        });
    }
}
