package org.example;

import java.util.List;

public class ScheduledBatchNotification extends Notification {
    private final List<String> recipients;

    public ScheduledBatchNotification(
            MessageSender sender, List<String> recipients) {
        super(sender);
        this.recipients = List.copyOf(recipients);
    }

    @Override
    public void send(String recipient, String message) {
        int successful = 0;
        int failed = 0;

        for (String user : recipients) {
            boolean sent = sender.isAvailable() &&
                    sender.sendMessage(user, "Batch", message);

            if (sent) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("Total: " + recipients.size());
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }
}
