package org.example;

public class UrgentNotification extends Notification {
    private MessageSender fallbackSender;

    public UrgentNotification(MessageSender sender) {
        super(sender);
    }

    public void setFallbackSender(MessageSender fallbackSender) {
        this.fallbackSender = fallbackSender;
    }

    @Override
    public void send(String recipient, String message) {
        String text = "[URGENT] " + message;

        if (sender.isAvailable() &&
                sender.sendMessage(recipient, "Urgent", text)) {
            return;
        }

        System.out.println("Primary sender failed.");

        if (fallbackSender != null && fallbackSender.isAvailable()) {
            boolean success = fallbackSender.sendMessage(
                    recipient, "Urgent", text
            );
            System.out.println("Fallback success: " + success);
        } else {
            System.out.println("No available fallback.");
        }
    }
}
