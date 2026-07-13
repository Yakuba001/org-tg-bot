package com.orgtgbot.repository;

import com.orgtgbot.entity.bookkeeper.BookkeeperRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Repository
public interface BookkeeperRepository extends JpaRepository<BookkeeperRecord, Long> {

    @Query("SELECT COALESCE(SUM(b.price), 0) FROM BookkeeperRecord b " +
            "WHERE b.telegramChatId = :chatId AND b.purchaseDate BETWEEN :start AND :end")
    BigDecimal getTotalSpendForPeriod(@Param("chatId") Long chatId,
                                      @Param("start") LocalDate start,
                                      @Param("end") LocalDate end);

    Page<BookkeeperRecord> findAllByTelegramChatIdOrderByPurchaseDateDesc(Long telegramChatId, Pageable pageable);
}
