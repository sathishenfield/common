package com.core.lib.exception;

import com.core.lib.constant.ErrorConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
@Component
public class CommonMessage {

    private static final String BASE_NAME = "messages";
    private final Locale defaultLocale = new Locale(ErrorConstant.LOCALE_MESSAGE_ENGLISH,
            ErrorConstant.LOCALE_MESSAGE_COUNTRYCODE);

    // Cache bundles to avoid reloading on every lookup
    private final ConcurrentMap<Locale, ResourceBundle> bundleCache = new ConcurrentHashMap<>();

    /**
     * Get localized message for a given key.
     *
     * @param messageKey key in the properties file
     * @return localized message if found, otherwise returns the key itself
     */
    public String getMessage(String messageKey) {
        return getMessage(messageKey, defaultLocale);
    }

    /**
     * Get localized message for a given key and locale.
     *
     * @param messageKey key in the properties file
     * @param locale     desired locale
     * @return localized message if found, otherwise returns the key itself
     */
    public String getMessage(String messageKey, Locale locale) {
        try {
            ResourceBundle bundle = bundleCache.computeIfAbsent(locale, loc ->
                    ResourceBundle.getBundle(BASE_NAME, loc));

            if (bundle.containsKey(messageKey)) {
                return bundle.getString(messageKey);
            } else {
                log.warn("Message key [{}] not found in bundle [{}] for locale [{}]",
                        messageKey, BASE_NAME, locale);
            }
        } catch (MissingResourceException e) {
            log.error("Resource bundle [{}] not found for locale [{}]: {}",
                    BASE_NAME, locale, e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while fetching message for key [{}] and locale [{}]: {}",
                    messageKey, locale, e.getMessage(), e);
        }

        // fallback to key itself
        return messageKey;
    }
}