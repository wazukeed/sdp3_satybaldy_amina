package org.example;

public class KafkaEventSender implements MessageSender {
    private boolean available = true;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) return false;

        System.out.println("[Kafka] Event published");
        System.out.println("Topic: notifications");
        System.out.println("Key: " + recipient);
        System.out.println("Payload: " + title + " - " + body);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }
}
