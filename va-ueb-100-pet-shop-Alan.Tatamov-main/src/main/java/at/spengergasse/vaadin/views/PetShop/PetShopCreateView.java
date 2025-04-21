package at.spengergasse.vaadin.views.PetShop;

import at.spengergasse.vaadin.domain.PetShop;
import at.spengergasse.vaadin.service.PetShopService;
import at.spengergasse.vaadin.views.MainLayout;
import at.spengergasse.vaadin.views.PetShop.PetShopListView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
@Route(value = "petshop-form/:ANIMAL_ID?", layout = MainLayout.class)
@PageTitle("PetShop Formular")
@Menu(title = "PetShop erstellen", icon = LineAwesomeIconUrl.ACCUSOFT)


public class PetShopCreateView extends VerticalLayout implements BeforeEnterObserver
{
    private final TextField name = new TextField("Name");
    private final TextField address = new TextField();


    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Abbrechen", LineAwesomeIcon.ADDRESS_BOOK.create(), new ComponentEventListener<ClickEvent<Button>>() {
        @Override
        public void onComponentEvent(ClickEvent<Button> e) {
            onCancel();
        }
    });

    private final BeanValidationBinder<PetShop> binder = new BeanValidationBinder<>(PetShop.class);
    private final PetShopService petshopService;

    private PetShop petshop = new PetShop();

    public PetShopCreateView(@Autowired PetShopService petShopService)
    {
        this.petshopService = petShopService;
        init();
    }

    private void init()
    {
        FormLayout formLayout = new FormLayout();
        name.setLabel("Name");
        address.setLabel("Address");

        formLayout.add(name, address);


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
            binder.writeBean(petshop);
            log.debug("PetShop unsaved = {}", petshop);
            petshopService.save(petshop);
            log.debug("Animal saved = {}", petshopService.findById(petshop.getId()));

            Notification.show("Daten wurden gespeichert");

            Optional<UI> uiOptional = getUI();
            uiOptional.ifPresent(new Consumer<UI>()
            {
                @Override
                public void accept(UI ui)
                {
                    ui.navigate(PetShopListView.class);
                }
            });

        }   catch (IllegalArgumentException e){
            log.error("Ungültige Eingabe beim Speichern des PetShops: {}", e.getMessage());
            Notification.show("Ungültige Eingabe: " + e.getMessage());
        }   catch (Exception e){
            log.error("Fehler beim Speichern des PetShops={}", e.getMessage());
            Notification.show("Ein unerwarteter Fehler ist aufgetreten beim Speichern." + e.getMessage());
        }
    }

    private void onCancel()
    {
        UI.getCurrent().navigate(PetShopListView.class);
    }

    private void setPetShop(PetShop petshop){
        this.petshop = petshop;
        binder.readBean(petshop);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        event.getRouteParameters().get("petshop_ID").ifPresent(new Consumer<String>()
        {
            @Override
            public void accept(String animalId)
            {
                petshopService.findById( Long.parseLong(animalId)).ifPresent(new Consumer<PetShop>()
                {
                    @Override
                    public void accept(PetShop petshop)
                    {
                        setPetShop(petshop);
                    }
                });
            }
        });
    }
}
