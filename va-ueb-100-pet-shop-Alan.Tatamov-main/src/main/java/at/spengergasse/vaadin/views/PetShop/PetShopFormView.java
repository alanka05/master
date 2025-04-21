package at.spengergasse.vaadin.views.PetShop;

import at.spengergasse.vaadin.domain.PetShop;
import at.spengergasse.vaadin.service.PetShopService;
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
@Route(value = "petshop/edit/:petshopId?", layout = MainLayout.class)
@PageTitle("PetShop erfassen / bearbeiten")
@Menu(title = "PetShop erfassen", icon = "la la-paw")
public class PetShopFormView extends VerticalLayout implements BeforeEnterObserver {

    private final TextField name = new TextField("Name");
    private final TextField address = new TextField("Adresse");


    private final Button saveButton = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen", LineAwesomeIcon.ADDRESS_CARD.create(), new ComponentEventListener<ClickEvent<Button>>()
    {
        @Override
        public void onComponentEvent(ClickEvent<Button> event)
        {
            onCancel();
        }
    });


    private final BeanValidationBinder<PetShop> binder = new BeanValidationBinder<>(PetShop.class);
    private final PetShopService petShopService;

    private PetShop petShop = new PetShop();

    public PetShopFormView(@Autowired PetShopService petShopService) {
        this.petShopService = petShopService;
        init();
    }

    private void init() {
        FormLayout formLayout = new FormLayout();


        name.setLabel("Name");
        address.setLabel("Adresse");


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

        binder.setBean(new PetShop());
    }

    private void onSave() {
        try
        {
            binder.writeBean(petShop);
            log.debug("PetShop unsaved : {}", petShop);
            petShopService.save(petShop);
            log.debug("PetShop saved : {}", petShopService.findById(petShop.getId()));

            Notification.show("PetShop saved");

            Optional<UI> uiOptional = getUI();
            uiOptional.ifPresent(new Consumer<UI>()
            {

                @Override
                public void accept(UI ui)
                {
                    ui.navigate(PetShopListView.class);
                }
            });

        }catch (IllegalArgumentException e){
            log.error(e.getMessage());
            Notification.show(e.getMessage());

        } catch (Exception e) {
            log.error("Fehler beim Speichern", e.getMessage());
            Notification.show("Fehler beim Speichern: " + e.getMessage(), 4000, Notification.Position.MIDDLE);
        }
    }

    private void onCancel()
    {
        UI.getCurrent().navigate(PetShopListView.class);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<String> param = event.getRouteParameters().get("PetShopId");

        param.ifPresent(petShopIdStr -> {
            try {
                Long petShopId = Long.parseLong(petShopIdStr);
                petShopService.findById(petShopId).ifPresentOrElse(
                        petShop -> binder.setBean(petShop),
                        () -> {
                            Notification.show("PetShop nicht gefunden.");
                            UI.getCurrent().navigate(PetShopListView.class);
                        }
                );
            } catch (NumberFormatException e) {
                Notification.show("Ungültige PetShop-ID.");
                UI.getCurrent().navigate(PetShopListView.class);
            }
        });
    }
}
