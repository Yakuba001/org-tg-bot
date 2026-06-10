package com.orgtgbot.bot.migration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseMigrationListener {

    @PersistenceContext
    private final EntityManager entityManager;

    @EventListener(ContextRefreshedEvent.class)
    @Order(Ordered.HIGHEST_PRECEDENCE) // Гарантируем наивысший приоритет до старта бота
    @Transactional
    public void onApplicationEvent() {
        log.info("[MIGRATION] Начало блокирующего патча БД: удаление устаревшего констрейнта...");
        try {
            // Выполняем нативный SQL напрямую в структуре PostgreSQL
            int updatedRows = entityManager.createNativeQuery(
                    "ALTER TABLE state_manager DROP CONSTRAINT IF EXISTS fkkjfh7xql4lhy4xa9ipqi4lmmo"
            ).executeUpdate();

            log.info("[MIGRATION] Патч успешно применен. Констрейнт удален. Строк затронуто: {}", updatedRows);
        } catch (Exception e) {
            log.error("[MIGRATION] Критическая ошибка при удалении констрейнта: {}", e.getMessage());
        }
    }
}
