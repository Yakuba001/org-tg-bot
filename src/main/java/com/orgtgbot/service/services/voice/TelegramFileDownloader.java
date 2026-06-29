package com.orgtgbot.service.services.voice;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;

@Component
public class TelegramFileDownloader {

    public byte[] downloadAsBytes(String downloadUrl) throws Exception {
        try (InputStream inputStream = URI.create(downloadUrl).toURL().openStream()) {
            return inputStream.readAllBytes();
        }
    }
}
