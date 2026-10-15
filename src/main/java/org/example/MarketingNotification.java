package org.example;

public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        String text = "Special offer! " + message;

        if (sender.isAvailable()) {
            sender.sendMessage(recipient, "Marketing", text);
        } else {
            System.out.println("Marketing delivery failed.");
        }
    }
}
