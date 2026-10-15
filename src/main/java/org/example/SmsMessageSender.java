package org.example;

public class SmsMessageSender implements MessageSender {
    private boolean available = true;
    private int maxLength = 160;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setMaxLength(int maxLength) {
        if (maxLength <= 0) {
            throw new IllegalArgumentException("Invalid SMS limit");
        }
        this.maxLength = maxLength;
    }

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available || body == null || body.length() > maxLength) {
            return false;
        }

        System.out.println("[SMS] To: " + recipient);
        System.out.println(body);
        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }
}
