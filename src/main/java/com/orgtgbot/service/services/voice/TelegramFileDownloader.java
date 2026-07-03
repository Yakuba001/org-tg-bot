package com.orgtgbot.service.services.voice;

import com.orgtgbot.exception.exceptions.voice.DownloadFileFromTelegramException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Component
public class TelegramFileDownloader {

    public byte[] downloadAsBytes(String downloadUrl) {
        try (InputStream inputStream = URI.create(downloadUrl).toURL().openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new DownloadFileFromTelegramException("Failed to download file from telegram", e);
        }
    }
}
