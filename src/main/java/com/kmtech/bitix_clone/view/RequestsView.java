package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.model.Equipment;
import com.kmtech.bitix_clone.model.Request;
import com.kmtech.bitix_clone.model.RequestStatus;
import com.kmtech.bitix_clone.model.RequestType;
import com.kmtech.bitix_clone.repository.ClientRepository;
import com.kmtech.bitix_clone.repository.EquipmentRepository;
import com.kmtech.bitix_clone.repository.RequestRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Route(value = "requests", layout = MainLayout.class)
@PageTitle("Заявки")
public class RequestsView extends VerticalLayout {

    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final EquipmentRepository equipmentRepository;
    private Grid<Request> grid = new Grid<>(Request.class);
    private ComboBox<Clients> clientComboBox = new ComboBox<>("Клиент");
    private ComboBox<Equipment> equipmentComboBox = new ComboBox<>("Оборудование");
    private ComboBox<RequestType> typeComboBox = new ComboBox<>("Тип заявки");
    private TextArea descriptionField = new TextArea("Описание заявки");
    private Button addButton = new Button("Создать заявку", e -> createRequest());

    @Autowired
    public RequestsView(RequestRepository requestRepository, ClientRepository clientRepository, EquipmentRepository equipmentRepository) {
        this.requestRepository = requestRepository;
        this.clientRepository = clientRepository;
        this.equipmentRepository = equipmentRepository;

        // Настройка грида
        grid.setColumns("id", "description", "type", "status");
        grid.addColumn(request -> request.getClient() != null ? request.getClient().getFirstName() + " " + request.getClient().getLastName() : "N/A").setHeader("Клиент");
        grid.addColumn(request -> request.getEquipment() != null ? request.getEquipment().getBrand() + " " + request.getEquipment().getModel() : "N/A").setHeader("Оборудование");
        grid.addColumn(Request::getCreatedAt).setHeader("Дата создания");

        // Форма для создания заявки
        List<Clients> clients = clientRepository.findAll();
        List<Equipment> equipmentList = equipmentRepository.findAll();
        clientComboBox.setItems(clients);
        clientComboBox.setItemLabelGenerator(client -> client.getFirstName() + " " + client.getLastName());
        equipmentComboBox.setItems(equipmentList);
        equipmentComboBox.setItemLabelGenerator(equipment -> equipment.getBrand() + " " + equipment.getModel());
        typeComboBox.setItems(RequestType.values());

        HorizontalLayout form = new HorizontalLayout(clientComboBox, equipmentComboBox, typeComboBox, descriptionField, addButton);
        form.setAlignItems(Alignment.BASELINE);

        add(new H2("Заявки"), form, grid);
        refreshGrid();
    }

    private void createRequest() {
        Request request = new Request();
        request.setClient(clientComboBox.getValue());
        request.setEquipment(equipmentComboBox.getValue());
        request.setType(typeComboBox.getValue());
        request.setDescription(descriptionField.getValue());
        request.setStatus(RequestStatus.NEW);
        request.setCreatedAt(LocalDateTime.now());
        requestRepository.save(request);
        refreshGrid();
        descriptionField.clear();
        clientComboBox.clear();
        equipmentComboBox.clear();
        typeComboBox.clear();
    }

    private void refreshGrid() {
        grid.setItems(requestRepository.findAll());
    }
}