package com.orgtgbot.service.voice;

import com.orgtgbot.service.services.voice.TelegramFileDownloader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

public class TelegramFileDownloaderTest {

    private final TelegramFileDownloader downloader = new TelegramFileDownloader();

    @Test
    void downloadAsBytes_ShouldReadFileFromUrlCorrectly(@TempDir Path tempDir) throws Exception {
        Path fakeFile = tempDir.resolve("test_voice.ogg");
        String testData = "Фейковые байты голосового сообщения";
        Files.writeString(fakeFile, testData);
        String fileUrl = fakeFile.toUri().toURL().toString();

        byte[] resultBytes = downloader.downloadAsBytes(fileUrl);

        assertThat(resultBytes).containsExactly(testData.getBytes());
    }
}
