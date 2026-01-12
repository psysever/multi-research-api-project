package com.research2.api.domain.kafka.utill;


import com.research2.api.domain.kafka.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OrderIdService {
    private final ShopRepository shopRepository;

    public String getOrCreateOrderId(String mbId, int ctDirect) {

        // 1️1.Check existing order number
        String existOdId =
                shopRepository.findLatestOrderId(mbId, ctDirect);

        if (existOdId != null) {
            return existOdId;
        }

        // 2. If not, create a new one
        return generateOrderId();
    }

    /**
     * exm : 2025121512304599
     */
    private String generateOrderId() {

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String base = now.format(formatter);

        String micro =
                String.format("%02d", now.getNano() / 10_000);

        return base + micro;
    }
}
