package org.example;

public class PushNotificationSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) return false;

        System.out.println("[Push] Device: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Message: " + body);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Push";
    }
}
