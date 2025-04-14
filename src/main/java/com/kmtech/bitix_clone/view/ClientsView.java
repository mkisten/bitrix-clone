package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.service.ClientService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Route(value = "clients", layout = MainLayout.class)
@PageTitle("Клиенты")
public class ClientsView extends VerticalLayout {

    private final ClientService clientService;
    private final Grid<Clients> clientGrid = new Grid<>(Clients.class);

    @Autowired
    public ClientsView(ClientService clientService) {
        this.clientService = clientService;

        setupGrid();
        updateGrid();

        add(clientGrid);
    }

    private void setupGrid() {
        clientGrid.setColumns("lastName", "firstName", "surname", "email", "phone");
        clientGrid.getColumnByKey("lastName").setHeader("Фамилия");
        clientGrid.getColumnByKey("firstName").setHeader("Имя");
        clientGrid.getColumnByKey("surname").setHeader("Отчество"); // Добавляем отчество
        clientGrid.getColumnByKey("email").setHeader("Email");
        clientGrid.getColumnByKey("phone").setHeader("Телефон");
    }

    private void updateGrid() {
        clientGrid.setItems(clientService.getAllClients());
    }
}