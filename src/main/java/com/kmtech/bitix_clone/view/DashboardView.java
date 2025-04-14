package com.kmtech.bitix_clone.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("dashboard") // Пример маршрута
public class DashboardView extends VerticalLayout {

    public DashboardView() {
        Button logoutButton = new Button("Logout", event -> {
            // Выполняем POST-запрос для выхода
            getUI().ifPresent(ui -> ui.getPage().executeJs(
                    "var form = document.createElement('form');" +
                            "form.method = 'POST';" +
                            "form.action = '/logout';" +
                            "document.body.appendChild(form);" +
                            "form.submit();"
            ));
        });
        add(logoutButton);
    }
}
