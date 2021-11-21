package com.github.sandokandias.payments.infrastructure.persistence.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sandokandias.payments.domain.entity.PaymentEventRepository;
import com.github.sandokandias.payments.domain.event.PaymentEvent;
import com.github.sandokandias.payments.domain.vo.PaymentEventId;
import com.github.sandokandias.payments.infrastructure.persistence.mapping.PaymentEventTable;
import com.github.sandokandias.payments.infrastructure.util.json.JsonMapper;
import io.vavr.Tuple;
import io.vavr.collection.Map;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static io.vavr.collection.HashMap.ofEntries;

@Repository
class PaymentEventRepositoryImpl implements PaymentEventRepository {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentEventRepositoryImpl.class);

    private static final String UPSERT_PAYMENT_EVENT =
            "INSERT INTO payment_event VALUES (:event_id, :event_type::payment_event_type, :payment_id, :timestamp, cast(:event_data AS JSON)) " +
            "ON CONFLICT (event_id) " +
            "DO UPDATE SET event_data = cast(:event_data AS JSON)";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JsonMapper jsonMapper;

    PaymentEventRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate,
                               JsonMapper jsonMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jsonMapper = jsonMapper;
    }

    @Override
    public CompletionStage<PaymentEventId> store(PaymentEvent paymentEvent) {
        LOG.debug("Storing paymentEvent {}", paymentEvent);
        final JsonNode eventDataAsJson = jsonMapper.convertToJsonNode(paymentEvent);
        LOG.debug("eventDataAsJson {}", eventDataAsJson);
        PaymentEventTable paymentEventTable = new PaymentEventTable();
        paymentEventTable.setEventId(paymentEvent.getEventId().id);
        paymentEventTable.setEventType(paymentEvent.getEventType());
        paymentEventTable.setPaymentId(paymentEvent.getPaymentId().id);
        paymentEventTable.setTimestamp(paymentEvent.getTimestamp());
        paymentEventTable.setEventData(eventDataAsJson);
        upsert(paymentEventTable);
        return CompletableFuture.completedFuture(paymentEvent.getEventId());
    }

    public void upsert(PaymentEventTable paymentEventTable) {
        Map<String, Object> params = ofEntries(
                Tuple.of("event_id", paymentEventTable.getEventId()),
                Tuple.of("event_type", paymentEventTable.getEventType().name()),
                Tuple.of("payment_id", paymentEventTable.getPaymentId()),
                Tuple.of("timestamp", LocalDateTime.now()),
                Tuple.of("event_data", Try.of(() -> jsonMapper.write(paymentEventTable.getEventData())).get())
        );

        jdbcTemplate.update(UPSERT_PAYMENT_EVENT, params.toJavaMap());
    }
}
