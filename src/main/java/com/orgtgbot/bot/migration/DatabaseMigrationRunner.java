package com.orgtgbot.bot.migration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseMigrationRunner implements CommandLineRunner {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        log.info("[MIGRATION] Запуск одноразового патча базы данных: удаление старого констрейнта...");
        try {
            // Выполняем нативный SQL для удаления физического ограничения
            int updatedRows = entityManager.createNativeQuery(
                    "ALTER TABLE state_manager DROP CONSTRAINT IF EXISTS fkkjfh7xql4lhy4xa9ipqi4lmmo"
            ).executeUpdate();

            log.info("[MIGRATION] Патч успешно выполнен. Ограничение удалено. Строк затронуто: {}", updatedRows);
        } catch (Exception e) {
            log.warn("[MIGRATION] Не удалось удалить констрейнт или он уже был удален ранее. Ошибка: {}", e.getMessage());
        }
    }
}
