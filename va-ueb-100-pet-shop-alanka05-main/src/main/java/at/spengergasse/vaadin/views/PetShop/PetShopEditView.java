package at.spengergasse.vaadin.views.PetShop;

import at.spengergasse.vaadin.domain.PetShop;
import at.spengergasse.vaadin.service.PetShopService;
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
@Route(value = "petshop-edit/:petshopId", layout = MainLayout.class)
@PageTitle("Tier bearbeiten")


public class PetShopEditView extends VerticalLayout implements BeforeEnterObserver
{

    private final TextField name = new TextField("Name");
    private final TextField address = new TextField("Adresse");



    private final Button saveButton = new Button("Speichern");
    private final Button cancelButton = new Button("Abbrechen",  e -> UI.getCurrent().navigate(PetShopListView.class));

    private final BeanValidationBinder<PetShop> binder = new BeanValidationBinder<>(PetShop.class);
    private final PetShopService petshopService;

    private PetShop petshop = new PetShop();

    public PetShopEditView(PetShopService petshopService)
    {
        this.petshopService = petshopService;
        init();
    }

    private void init(){
        FormLayout formLayout = new FormLayout();

        formLayout.add(name, address);

        saveButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        saveButton.addClickListener(e -> onSave());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);

        add(formLayout, buttonLayout);
        binder.bindInstanceFields(this);
    }

    private void onSave()
    {
        try{
            binder.writeBean(petshop);
            petshopService.save(petshop);
            Notification.show("PetShop erfolgreich gespeichert", 3000, Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate(PetShopListView.class);
        } catch(Exception e){
            log.error("Fehler beim Speichern", e.getMessage());
            Notification.show("Fehler beim Speichern" + e.getMessage());
        }
    }

    private void setPetShop(PetShop petshop){
        this.petshop = petshop;
        binder.readBean(petshop);
    }


    @Override
    public void beforeEnter(BeforeEnterEvent event){
        Optional<String> paramOptional = event.getRouteParameters().get(PetShopListView.PARAM_PETSHOP_ID);

        paramOptional.ifPresent(new Consumer<String>()
        {
            @Override
            public void accept(String petshopIdString)
            {
                try{
                    Long petshopId = Long.parseLong(petshopIdString);

                    Optional<PetShop> petshopOptional = petshopService.findById(petshopId);

                    if(petshopOptional.isPresent()){
                        PetShop petshop= petshopOptional.get();
                        binder.setBean(petshop);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Fehler beim Speichern");
                }

            }
        });
    }
}
