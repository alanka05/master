package at.spengergasse.vaadin.views.Animal;

import at.spengergasse.vaadin.domain.Animal;
import at.spengergasse.vaadin.service.AnimalService;
import at.spengergasse.vaadin.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.jsoup.select.Evaluator;
import org.vaadin.lineawesome.LineAwesomeIconUrl;
import com.vaadin.flow.router.RouteParameters;

import java.util.function.Consumer;

import static org.reflections.Reflections.log;

@Route(value = "animallist", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Animal List")
@Menu(title = "Animal List", icon = LineAwesomeIconUrl.ADOBE)
public class AnimalListView extends VerticalLayout {

    public static final String PARAM_ANIMAL_ID = "animalId";
    private final AnimalService animalService;
    private final Grid<Animal> grid = new Grid<>(Animal.class, false);

    public AnimalListView(AnimalService animalService) {
        this.animalService = animalService;
        configureGrid();
        add(grid);
        setSizeFull();
        grid.setSizeFull();
        reload();
    }

    private void configureGrid() {
        grid.addColumn(Animal::getName).setHeader("Name").setSortable(true);
        grid.addColumn(Animal::getColor).setHeader("Color").setSortable(true);
        grid.addColumn(Animal::getType).setHeader("Type").setSortable(true);
        grid.addColumn(Animal::getPrice).setHeader("Price").setSortable(true);

        grid.addComponentColumn(this::buildActionButtons).setHeader("Actions");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }

    private Component buildActionButtons(Animal animal) {
        Button edit = new Button("Edit", e -> onEdit(animal.getId()));
        Button delete = new Button("Delete", e -> onDelete(animal.getId()));
        return new HorizontalLayout(edit, delete);
    }


    private void onEdit(Long animalId) {
        UI.getCurrent().navigate(AnimalEditView.class,
                new RouteParameters(PARAM_ANIMAL_ID, animalId.toString()));
    }


    private void onDelete(Long id) {
        try {
            boolean success = animalService.deleteById(id);

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
        grid.setItems(animalService.findAll());
    }

}
