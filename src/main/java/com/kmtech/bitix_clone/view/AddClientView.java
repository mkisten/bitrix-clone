package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.Clients;
import com.kmtech.bitix_clone.service.ClientService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
@Route(value = "add-client", layout = MainLayout.class)
@PageTitle("Добавить клиента")
public class AddClientView extends VerticalLayout {

    private final ClientService clientService;
    private final TextField firstName = new TextField("Имя");
    private final TextField lastName = new TextField("Фамилия");
    private final TextField surname = new TextField("Отчество"); // Новое поле
    private final TextField email = new TextField("Email");
    private final TextField phone = new TextField("Телефон");
    private final Button saveButton = new Button("Сохранить");

    @Autowired
    public AddClientView(ClientService clientService) {
        this.clientService = clientService;

        FormLayout formLayout = new FormLayout();
        formLayout.add(firstName, lastName, surname, email, phone); // Добавляем surname
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));

        saveButton.addClickListener(event -> saveClient());

        add(formLayout, saveButton);
    }

    private void saveClient() {
        try {
            Clients client = new Clients();
            client.setFirstName(firstName.getValue());
            client.setLastName(lastName.getValue());
            client.setSurname(surname.getValue()); // Устанавливаем отчество
            client.setEmail(email.getValue());
            client.setPhone(phone.getValue());

            clientService.createClient(client);
            Notification.show("Клиент успешно сохранен!", 3000, Notification.Position.TOP_CENTER);
            clearForm();
        } catch (Exception e) {
            Notification.show("Ошибка при сохранении клиента: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }

    private void clearForm() {
        firstName.clear();
        lastName.clear();
        surname.clear(); // Очищаем поле отчества
        email.clear();
        phone.clear();
    }
}