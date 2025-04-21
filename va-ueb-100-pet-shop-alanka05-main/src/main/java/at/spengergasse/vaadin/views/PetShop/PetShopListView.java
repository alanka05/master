package at.spengergasse.vaadin.views.PetShop;

import at.spengergasse.vaadin.domain.PetShop;
import at.spengergasse.vaadin.service.PetShopService;
import at.spengergasse.vaadin.views.Animal.AnimalEditView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.jsoup.select.Evaluator;
import org.vaadin.lineawesome.LineAwesomeIconUrl;


import static org.reflections.Reflections.log;

@Route("petshoplist")
@PageTitle("PetShop List")
@Menu(title = "PetShop List", icon = LineAwesomeIconUrl.FIGMA)
public class PetShopListView extends VerticalLayout {

    public static final String PARAM_PETSHOP_ID = "petshopId";
    private final PetShopService petShopService;
    private final Grid<PetShop> grid = new Grid<>(PetShop.class, false);

    public PetShopListView(PetShopService petshopService) {
        this.petShopService = petshopService;
        configureGrid();
        add(grid);
        setSizeFull();
        grid.setSizeFull();
        reload();
    }

    private void configureGrid() {
        grid.addColumn(PetShop::getName).setHeader("Name").setSortable(true);
        grid.addColumn(PetShop::getAddress).setHeader("Address").setSortable(true);

        grid.addComponentColumn(this::buildActionButtons).setHeader("Actions");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }

    private Component buildActionButtons(PetShop petshop) {
        Button edit = new Button("Edit", e -> onEdit(petshop.getId()));
        Button delete = new Button("Delete", e -> onDelete(petshop.getId()));
        return new HorizontalLayout(edit, delete);
    }

    private void onEdit(Long petshopId) {
        UI.getCurrent().navigate(PetShopEditView.class,
                new RouteParameters(PARAM_PETSHOP_ID, petshopId.toString()));
    }

    private void onDelete(Long id) {
        try {
            boolean success = petShopService.deleteById(id);

            if(success)
            {
                Notification.show("Tier erfolgreich gelöscht.");
                reload();
            } else {
                Notification.show("Fehler beim Löschen vom Tier");

            }
        } catch (IllegalArgumentException e) {
            Notification.show("Systemfehler im Service: " + e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            Notification.show("Systemfehler: " + e.getMessage());
        }
    }

    private void reload() {
        grid.setItems(petShopService.findAll());
    }

}
