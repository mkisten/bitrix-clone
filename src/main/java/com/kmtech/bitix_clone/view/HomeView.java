package com.kmtech.bitix_clone.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Greemdom")
public class HomeView extends VerticalLayout {

    public HomeView() {
        add(new H1("Добро пожаловать в Greemdom!"));
        add(new com.vaadin.flow.component.html.Paragraph("Выберите раздел в меню для работы с заявками, клиентами, оборудованием или отправкой email."));
    }
}