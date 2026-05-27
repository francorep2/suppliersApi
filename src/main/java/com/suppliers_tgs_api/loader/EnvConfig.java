package com.suppliers_tgs_api.loader;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    public static void load() {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("user.timezone", "UTC");
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USER", dotenv.get("DB_USER"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("ENCRYPTION_KEY", dotenv.get("ENCRYPTION_KEY"));
        System.setProperty("ALGORITHM", dotenv.get("ALGORITHM"));
    }
}