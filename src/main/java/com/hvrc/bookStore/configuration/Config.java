package com.hvrc.bookStore.configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.load();

    public static String getTwilioSid() {
        return dotenv.get("TWILIO_ACCOUNT_SID");
    }
}
