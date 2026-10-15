
import org.example.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationTest {

    private String capture(Runnable action) {
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(output, true, StandardCharsets.UTF_8));
            action.run();
        } finally {
            System.setOut(original);
        }
        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void telegramSuccess() {
        TelegramMessageSender sender = new TelegramMessageSender();
        assertTrue(sender.sendMessage("user", "Hello", "Message"));
    }

    @Test
    void telegramUnavailable() {
        TelegramMessageSender sender = new TelegramMessageSender();
        sender.setAvailable(false);
        assertFalse(sender.sendMessage("user", "Hello", "Message"));
    }

    @Test
    void urgentAddsPrefix() {
        UrgentNotification notification =
                new UrgentNotification(new TelegramMessageSender());
        String output = capture(() -> notification.send("user", "Help"));
        assertTrue(output.contains("[URGENT] Help"));
    }

    @Test
    void fallbackWorks() {
        TelegramMessageSender telegram = new TelegramMessageSender();
        telegram.setAvailable(false);

        UrgentNotification notification = new UrgentNotification(telegram);
        notification.setFallbackSender(new SmsMessageSender());

        String output = capture(() -> notification.send("123", "Help"));
        assertTrue(output.contains("[SMS]"));
        assertTrue(output.contains("Fallback success: true"));
    }

    @Test
    void smsAccepts160Characters() {
        SmsMessageSender sms = new SmsMessageSender();
        assertTrue(sms.sendMessage("123", "Title", "a".repeat(160)));
    }

    @Test
    void smsRejects161Characters() {
        SmsMessageSender sms = new SmsMessageSender();
        assertFalse(sms.sendMessage("123", "Title", "a".repeat(161)));
    }

    @Test
    void encryptionWorks() {
        EncryptedNotification notification =
                new EncryptedNotification(new EmailMessageSender());

        String output = capture(() -> notification.send("user", "Hello"));
        assertTrue(output.contains("SGVsbG8="));
        assertFalse(output.contains("Subject: Encrypted\nHello"));
    }

    @Test
    void batchSummaryWorks() {
        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        new SmsMessageSender(),
                        List.of("111", "222", "333")
                );

        String output = capture(() -> notification.send("", "Meeting"));
        assertTrue(output.contains("Total: 3"));
        assertTrue(output.contains("Successful: 3"));
        assertTrue(output.contains("Failed: 0"));
    }

    @Test
    void runtimeSwitchWorks() {
        UrgentNotification notification =
                new UrgentNotification(new TelegramMessageSender());

        notification.setSender(new EmailMessageSender());
        String output = capture(() -> notification.send("user", "Hello"));
        assertTrue(output.contains("[Email]"));
    }

    @Test
    void pushWorks() {
        PushNotificationSender push = new PushNotificationSender();
        assertTrue(push.sendMessage("device", "Title", "Body"));
    }

    @Test
    void marketingWorks() {
        MarketingNotification notification =
                new MarketingNotification(new PushNotificationSender());

        String output = capture(() -> notification.send("device", "Discount"));
        assertTrue(output.contains("Special offer! Discount"));
    }

    @Test
    void batchFailureWorks() {
        SmsMessageSender sms = new SmsMessageSender();
        sms.setAvailable(false);

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(sms, List.of("111", "222"));

        String output = capture(() -> notification.send("", "Hello"));
        assertTrue(output.contains("Successful: 0"));
        assertTrue(output.contains("Failed: 2"));
    }
}
