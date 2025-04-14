package com.kmtech.bitix_clone.view;

import com.kmtech.bitix_clone.model.EmailRequest;
import com.kmtech.bitix_clone.service.EmailServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "send-email", layout = MainLayout.class)
@PageTitle("Отправить Email")
public class SendEmailView extends VerticalLayout {

    private final EmailServiceImpl emailService;
    private TextField toField = new TextField("Кому");
    private TextField subjectField = new TextField("Тема");
    private TextArea textArea = new TextArea("Текст сообщения");
    private Button sendButton = new Button("Отправить", e -> sendEmail());

    @Autowired
    public SendEmailView(EmailServiceImpl emailService) {
        this.emailService = emailService;

        HorizontalLayout form = new HorizontalLayout(toField, subjectField, textArea, sendButton);
        form.setAlignItems(Alignment.BASELINE);

        add(new H2("Отправить Email"), form);
    }

    private void sendEmail() {
        try {
            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(toField.getValue());
            emailRequest.setSubject(subjectField.getValue());
            emailRequest.setText(textArea.getValue());
            emailService.sendSimpleEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getText());
            Notification.show("Email успешно отправлен!", 3000, Notification.Position.TOP_CENTER);
        } catch (Exception e) {
            Notification.show("Ошибка при отправке email: " + e.getMessage(), 3000, Notification.Position.TOP_CENTER);
        }
    }
}