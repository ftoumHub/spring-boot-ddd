package com.github.sandokandias.payments.domain.command;

import com.github.sandokandias.payments.domain.shared.Command;
import com.github.sandokandias.payments.domain.vo.*;
import io.vavr.API;
import lombok.Value;

import java.time.LocalDateTime;

public interface PaymentCommand extends Command {

    static API.Match.Pattern0<PerformPayment>  $PerformPayment() {
        return API.Match.Pattern0.of(PerformPayment.class);
    }

    static API.Match.Pattern0<ConfirmPayment>  $ConfirmPayment() {
        return API.Match.Pattern0.of(ConfirmPayment.class);
    }

    static API.Match.Pattern0<AuthorizePayment>  $AuthorizePayment() {
        return API.Match.Pattern0.of(AuthorizePayment.class);
    }

    @Value(staticConstructor = "commandOf")
    class PerformPayment implements PaymentCommand {
        private final CustomerId customerId;
        private final PaymentIntent paymentIntent;
        private final PaymentMethod paymentMethod;
        private final Transaction transaction;
        private final LocalDateTime timestamp = LocalDateTime.now();
    }

    @Value(staticConstructor = "commandOf")
    class ConfirmPayment implements PaymentCommand {
        private final PaymentId paymentId;
        private final CustomerId customerId;
        private final LocalDateTime timestamp = LocalDateTime.now();
    }

    @Value(staticConstructor = "commandOf")
    class AuthorizePayment implements PaymentCommand {
        private final PaymentId paymentId;
        private final CustomerId customerId;
        private final PaymentMethod paymentMethod;
        private final Transaction transaction;
        private final LocalDateTime timestamp = LocalDateTime.now();
    }
}
