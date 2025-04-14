package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.*;
import com.kmtech.bitix_clone.repository.AnnualMaintenanceRepository;
import com.kmtech.bitix_clone.service.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@SpringComponent
@UIScope
@Route(value = "client-card", layout = MainLayout.class)
@PageTitle("Карточка клиента")
public class ClientCardView extends VerticalLayout {

    private final ClientService clientService;
    private final ClientEquipmentService clientEquipmentService;
    private final BrandService brandService;
    private final ModelService modelService;
    private final RequestService requestService;
    private final AnnualMaintenanceRepository maintenanceRepository;

    private final ComboBox<Clients> clientComboBox = new ComboBox<>("Клиент");
    private final ComboBox<Brand> brandComboBox = new ComboBox<>("Марка");
    private final ComboBox<Model> modelComboBox = new ComboBox<>("Модель");
    private final TextField serialNumber = new TextField("Серийный номер");
    private final Button addEquipmentButton = new Button("Добавить оборудование");
    private final Grid<ClientEquipment> equipmentGrid = new Grid<>(ClientEquipment.class);
    private final Grid<Request> requestGrid = new Grid<>(Request.class);
    private final TextArea commentArea = new TextArea("Комментарий");

    @Autowired
    public ClientCardView(ClientService clientService, ClientEquipmentService clientEquipmentService,
                          BrandService brandService, ModelService modelService,
                          RequestService requestService, AnnualMaintenanceRepository maintenanceRepository) {
        this.clientService = clientService;
        this.clientEquipmentService = clientEquipmentService;
        this.brandService = brandService;
        this.modelService = modelService;
        this.requestService = requestService;
        this.maintenanceRepository = maintenanceRepository;

        setupClientComboBox();
        setupEquipmentForm();
        setupEquipmentGrid();
        setupRequestGrid();
        setupCommentArea();

        add(clientComboBox, new HorizontalLayout(brandComboBox, modelComboBox, serialNumber, addEquipmentButton),
                equipmentGrid, requestGrid, commentArea);
    }

    private void setupClientComboBox() {
        clientComboBox.setItems(clientService.getAllClients());
        clientComboBox.setItemLabelGenerator(client ->
                client.getLastName() + " " + client.getFirstName() +
                        (client.getSurname() != null ? " " + client.getSurname() : "")); // Добавляем отчество
        clientComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                updateEquipmentGrid(event.getValue());
                updateRequestGrid(event.getValue());
            }
        });
    }

    private void setupEquipmentForm() {
        brandComboBox.setItems(brandService.getAllBrands());
        brandComboBox.setItemLabelGenerator(Brand::getName);
        brandComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                modelComboBox.setItems(modelService.getModelsByBrand(event.getValue().getId()));
            } else {
                modelComboBox.clear();
            }
        });

        modelComboBox.setItemLabelGenerator(Model::getName);
        addEquipmentButton.addClickListener(event -> addEquipment());
    }

    private void setupEquipmentGrid() {
        equipmentGrid.setColumns("brand.name", "model.name", "serialNumber");
        equipmentGrid.getColumnByKey("brand.name").setHeader("Марка");
        equipmentGrid.getColumnByKey("model.name").setHeader("Модель");
        equipmentGrid.getColumnByKey("serialNumber").setHeader("Серийный номер");
    }

    private void setupRequestGrid() {
        requestGrid.setColumns("type", "status", "description", "createdAt", "completedAt");
        requestGrid.getColumnByKey("type").setHeader("Тип");
        requestGrid.getColumnByKey("status").setHeader("Статус");
        requestGrid.getColumnByKey("description").setHeader("Описание");
        requestGrid.getColumnByKey("createdAt").setHeader("Дата создания");
        requestGrid.getColumnByKey("completedAt").setHeader("Дата завершения");
    }

    private void setupCommentArea() {
        commentArea.setWidthFull();
        commentArea.setHeight("150px");
    }

    private void addEquipment() {
        if (clientComboBox.getValue() == null || brandComboBox.getValue() == null || modelComboBox.getValue() == null || serialNumber.isEmpty()) {
            Notification.show("Заполните все поля!", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        try {
            clientEquipmentService.createClientEquipment(
                    clientComboBox.getValue(),
                    brandComboBox.getValue().getId(),
                    modelComboBox.getValue().getId(),
                    serialNumber.getValue()
            );
            updateEquipmentGrid(clientComboBox.getValue());
            Notification.show("Оборудование добавлено!", 3000, Notification.Position.TOP_CENTER);
            clearEquipmentForm();
        } catch (Exception e) {
            Notification.show("Ошибка: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void updateEquipmentGrid(Clients client) {
        equipmentGrid.setItems(clientEquipmentService.getEquipmentByClient(client));
    }

    private void updateRequestGrid(Clients client) {
        List<Request> requests = requestService.findAll().stream()
                .filter(request -> request.getClient().getId().equals(client.getId()))
                .collect(Collectors.toList());
        requestGrid.setItems(requests);
    }

    private void clearEquipmentForm() {
        brandComboBox.clear();
        modelComboBox.clear();
        serialNumber.clear();
    }
}