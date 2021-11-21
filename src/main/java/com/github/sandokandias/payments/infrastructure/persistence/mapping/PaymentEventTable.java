package com.github.sandokandias.payments.infrastructure.persistence.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sandokandias.payments.domain.vo.PaymentEventType;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"eventData", "timestamp"})
@ToString
public class PaymentEventTable {
    //@Id
    private String eventId;
    private PaymentEventType eventType;
    private String paymentId;
    private LocalDateTime timestamp;
    //@Column(length = 1024)
    private JsonNode eventData;
}
