package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        PushNotificationSender push = new PushNotificationSender();

        // 1. Urgent + Telegram
        System.out.println("\n1. Urgent + Telegram");
        UrgentNotification urgent = new UrgentNotification(telegram);
        urgent.send("user1", "Server is down!");

        // 2. Encrypted + Email
        System.out.println("\n2. Encrypted + Email");
        EncryptedNotification encrypted = new EncryptedNotification(email);
        encrypted.send("user@email.com", "Secret message");

        // 3. Batch + SMS
        System.out.println("\n3. Batch + SMS");
        ScheduledBatchNotification batch = new ScheduledBatchNotification(
                sms, List.of("111", "222", "333")
        );
        batch.send("", "Meeting tomorrow");

        // 4. Runtime switching
        System.out.println("\n4. Telegram to Email");
        urgent.send("user1", "First message");
        urgent.setSender(email);
        urgent.send("user@email.com", "Second message");

        // 5. Fallback Telegram to SMS
        System.out.println("\n5. Fallback");
        telegram.setAvailable(false);
        urgent.setSender(telegram);
        urgent.setFallbackSender(sms);
        urgent.send("111", "Emergency!");

        // 6. Marketing + Push
        System.out.println("\n6. Marketing + Push");
        MarketingNotification marketing = new MarketingNotification(push);
        marketing.send("device123", "50% discount!");
    }
}
