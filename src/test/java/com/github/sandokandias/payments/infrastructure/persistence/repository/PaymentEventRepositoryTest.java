package com.github.sandokandias.payments.infrastructure.persistence.repository;

import com.github.sandokandias.payments.DddDataSource;
import com.github.sandokandias.payments.domain.event.PaymentAuthorized;
import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.Money;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.domain.vo.Transaction;
import com.github.sandokandias.payments.domain.vo.TransactionItem;
import com.github.sandokandias.payments.infrastructure.util.json.JsonMapper;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;

public class PaymentEventRepositoryTest {

    private PaymentAuthorized pe = PaymentAuthorized.eventOf(
            new PaymentId("PAY-2a3a0c07-7c35-40f7-908d-c7122b999fc5"),
            new CustomerId("GGN-1310"),
            PaymentMethod.CREDIT_CARD,
            new Transaction(null, Arrays.asList(new TransactionItem("dtc", new Money("BRL", 10, 2), 1))),
            LocalDateTime.now());

    PaymentEventRepositoryImpl paymentEventRepository = new PaymentEventRepositoryImpl(
            new NamedParameterJdbcTemplate(DddDataSource.getDataSource()),
            new JsonMapper());

    @Test
    public void store_shouldReturnPaymentEventId_whenAddingPaymentEvent() {

        paymentEventRepository.store(pe);
    }
}
