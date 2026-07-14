package com.orgtgbot.aggregator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Component
@Slf4j
public class MediaGroupAggregator {

    private static final long DEBOUNCE_DELAY_MS = 1500;
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<byte[]>> cache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Boolean> leaders = new ConcurrentHashMap<>();

    public void aggregate(String mediaGroupId, byte[] imageBytes, Consumer<List<byte[]>> onComplete) {
        List<byte[]> images = cache.computeIfAbsent(mediaGroupId, key -> new CopyOnWriteArrayList<>());

        images.add(imageBytes);
        if (leaders.putIfAbsent(mediaGroupId, Boolean.TRUE) == null) {
            log.info("Поток-Лидер запущен для медиагруппы: {}", mediaGroupId);
            try {
                Thread.sleep(DEBOUNCE_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Поток-Лидер был прерван", e);
            } finally {
                List<byte[]> collectedImages = cache.remove(mediaGroupId);
                leaders.remove(mediaGroupId);
                if (collectedImages != null && !collectedImages.isEmpty()) {
                    log.info("Медиагруппа {} успешно собрана. Найдено картинок: {}. Начинаем обработку...",
                            mediaGroupId, collectedImages.size());
                    onComplete.accept(collectedImages);
                }
            }
        } else {
            log.debug("Ведомый поток добавил картинку в группу {} и завершил работу.", mediaGroupId);
        }
    }
}
