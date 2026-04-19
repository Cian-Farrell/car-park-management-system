package com.carpark.service;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

// Localisation utility class
// Provides static methods to format currency and dates according to a given Locale
// Locale represents a specific region/language — e.g. Locale.UK, Locale.US, Locale.FRANCE
public class LocalisationUtil {

    // NumberFormat.getCurrencyInstance(Locale) formats a double as a currency string
    // The format (symbol, decimal separator) changes based on the Locale provided
    // e.g. Locale.UK → £2.50 | Locale.US → $2.50 | Locale.FRANCE → 2,50 €
    public static String formatCurrency(double amount, Locale locale) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);
        return currencyFormat.format(amount);
    }

    // DateTimeFormatter.ofLocalizedDateTime() formats a LocalDateTime using the Locale's
    // regional date/time conventions — day/month order, separators etc. all change by Locale
    // FormatStyle.MEDIUM gives a readable format e.g. "19 Apr 2024, 10:30:00"
    public static String formatDateTime(LocalDateTime dateTime, Locale locale) {
        DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(locale);
        return dateTime.format(formatter);
    }

    // Prints a demonstration of both formatters across multiple locales
    // Shows clearly how the same data looks different depending on the Locale
    public static void demonstrateLocalisation(double fee, LocalDateTime dateTime) {
        Locale[] locales = { Locale.UK, Locale.US, Locale.FRANCE, Locale.GERMANY };

        System.out.println("\n--- Localisation Demo ---");
        for (Locale locale : locales) {
            System.out.println("\nLocale: " + locale.getDisplayCountry());
            System.out.println("  Currency : " + formatCurrency(fee, locale));
            System.out.println("  Date/Time: " + formatDateTime(dateTime, locale));
        }
    }
}