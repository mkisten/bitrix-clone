package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.AnnualMaintenance;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "equipment/maintenance", layout = MainLayout.class)
@PageTitle("История обслуживания")
public class MaintenanceView extends VerticalLayout implements BeforeEnterObserver {

    private final AnnualMaintenanceRepository maintenanceRepository;
    private Grid<AnnualMaintenance> grid = new Grid<>(AnnualMaintenance.class);
    private Long equipmentId;

    @Autowired
    public MaintenanceView(AnnualMaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;

        grid.setColumns("id", "firstMaintenanceDate");
        grid.addColumn(maintenance -> maintenance.getClients() != null ? maintenance.getClients().getFirstName() + " " + maintenance.getClients().getLastName() : "N/A")
                .setHeader("Клиент");
        grid.addColumn(maintenance -> maintenance.getEquipment() != null ? maintenance.getEquipment().getBrand() + " " + maintenance.getEquipment().getModel() : "N/A")
                .setHeader("Оборудование");

        add(new H2("История обслуживания"), grid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String equipmentIdParam = event.getLocation().getQueryParameters().getParameters().getOrDefault("equipmentId", List.of("")).get(0);
        try {
            this.equipmentId = Long.parseLong(equipmentIdParam);
            grid.setItems(maintenanceRepository.findByEquipmentId(equipmentId));
        } catch (NumberFormatException e) {
            add(new com.vaadin.flow.component.html.Paragraph("Ошибка: Неверный ID оборудования"));
        }
    }
}