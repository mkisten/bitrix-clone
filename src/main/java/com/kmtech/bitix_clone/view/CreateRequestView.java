package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.*;
import com.kmtech.bitix_clone.service.ClientEquipmentService;
import com.kmtech.bitix_clone.service.ClientService;
import com.kmtech.bitix_clone.service.RequestService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringComponent
@UIScope
@Route(value = "create-request", layout = MainLayout.class)
@PageTitle("Создать наряд")
public class CreateRequestView extends VerticalLayout {

    private final RequestService requestService;
    private final ClientService clientService;
    private final ClientEquipmentService clientEquipmentService;

    private final ComboBox<Clients> clientComboBox = new ComboBox<>("Клиент");
    private final ComboBox<ClientEquipment> equipmentComboBox = new ComboBox<>("Оборудование");
    private final ComboBox<RequestType> typeComboBox = new ComboBox<>("Тип");
    private final ComboBox<RequestStatus> statusComboBox = new ComboBox<>("Статус");
    private final DatePicker createdAtPicker = new DatePicker("Дата создания");
    private final DatePicker completedAtPicker = new DatePicker("Дата завершения");
    private final TextArea descriptionArea = new TextArea("Описание");
    private final Button saveButton = new Button("Создать наряд");

    @Autowired
    public CreateRequestView(RequestService requestService, ClientService clientService,
                             ClientEquipmentService clientEquipmentService) {
        this.requestService = requestService;
        this.clientService = clientService;
        this.clientEquipmentService = clientEquipmentService;

        setupForm();
        FormLayout formLayout = new FormLayout();
        formLayout.add(clientComboBox, equipmentComboBox, typeComboBox, statusComboBox,
                createdAtPicker, completedAtPicker, descriptionArea);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        saveButton.addClickListener(event -> saveRequest());

        add(formLayout, saveButton);
    }

    private void setupForm() {
        clientComboBox.setItems(clientService.getAllClients());
        clientComboBox.setItemLabelGenerator(client ->
                client.getLastName() + " " + client.getFirstName() +
                        (client.getSurname() != null ? " " + client.getSurname() : "")); // Добавляем отчество
        clientComboBox.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                equipmentComboBox.setItems(clientEquipmentService.getEquipmentByClient(event.getValue()));
            } else {
                equipmentComboBox.clear();
            }
        });

        equipmentComboBox.setItemLabelGenerator(equipment ->
                equipment.getBrand().getName() + " " + equipment.getModel().getName() + " (" + equipment.getSerialNumber() + ")");

        typeComboBox.setItems(RequestType.values());
        statusComboBox.setItems(RequestStatus.values());

        createdAtPicker.setValue(LocalDate.now());
        descriptionArea.setWidthFull();
    }

    private void saveRequest() {
        if (clientComboBox.getValue() == null || typeComboBox.getValue() == null || statusComboBox.getValue() == null) {
            Notification.show("Заполните обязательные поля!", 3000, Notification.Position.TOP_CENTER);
            return;
        }

        try {
            Request request = new Request();
            request.setClient(clientComboBox.getValue());
            if (equipmentComboBox.getValue() != null) {
                Equipment equipment = new Equipment();
                equipment.setBrand(equipmentComboBox.getValue().getBrand().getName());
                equipment.setModel(equipmentComboBox.getValue().getModel().getName());
                request.setEquipment(equipment);
            }
            request.setType(typeComboBox.getValue());
            request.setStatus(statusComboBox.getValue());
            request.setDescription(descriptionArea.getValue());
            request.setCreatedAt(createdAtPicker.getValue().atStartOfDay());
            if (completedAtPicker.getValue() != null) {
                request.setCompletedAt(completedAtPicker.getValue().atStartOfDay());
            }

            requestService.createRequest(request);
            Notification.show("Наряд успешно создан!", 3000, Notification.Position.TOP_CENTER);
            clearForm();
        } catch (Exception e) {
            Notification.show("Ошибка: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        clientComboBox.clear();
        equipmentComboBox.clear();
        typeComboBox.clear();
        statusComboBox.clear();
        createdAtPicker.setValue(LocalDate.now());
        completedAtPicker.clear();
        descriptionArea.clear();
    }
}