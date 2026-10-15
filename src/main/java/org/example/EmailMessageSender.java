package org.example;

public class EmailMessageSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) return false;

        System.out.println("[Email] To: " + recipient);
        System.out.println("Subject: " + title);
        System.out.println(body);
        System.out.println("Best regards, Notification Gateway");
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }
}
