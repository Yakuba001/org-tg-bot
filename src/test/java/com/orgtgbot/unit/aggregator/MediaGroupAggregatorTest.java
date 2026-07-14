package com.orgtgbot.unit.aggregator;

import com.orgtgbot.aggregator.MediaGroupAggregator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.*;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MediaGroupAggregatorTest {

    @Test
    void should_AggregateMultipleImagesFromConcurrentThreads_And_TriggerOnCompleteOnce() throws InterruptedException {
        MediaGroupAggregator aggregator = new MediaGroupAggregator();
        String mediaGroupId = "test-media-group-777";
        int numberOfImages = 5;
        CopyOnWriteArrayList<List<byte[]>> finalResultCollector = new CopyOnWriteArrayList<>();
        CountDownLatch threadsLatch = new CountDownLatch(numberOfImages);

        try (ExecutorService testExecutor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfImages; i++) {
                final byte[] singleImageBytes = new byte[]{(byte) i};
                testExecutor.execute(() -> {
                    try {
                        aggregator.aggregate(mediaGroupId, singleImageBytes, finalResultCollector::add);
                    } finally {
                        threadsLatch.countDown();
                    }
                });
            }
        }

        assertTrue(threadsLatch.await(5, TimeUnit.SECONDS));
        await().atMost(3, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    assertEquals(1, finalResultCollector.size());
                    List<byte[]> totalCollected = finalResultCollector.getFirst();
                    assertEquals(numberOfImages, totalCollected.size());
                });
    }
}
