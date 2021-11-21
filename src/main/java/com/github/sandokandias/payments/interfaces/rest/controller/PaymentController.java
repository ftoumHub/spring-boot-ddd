package com.github.sandokandias.payments.interfaces.rest.controller;

import com.github.sandokandias.payments.application.PaymentProcessManager;
import com.github.sandokandias.payments.domain.command.PaymentCommand;
import com.github.sandokandias.payments.domain.vo.CustomerId;
import com.github.sandokandias.payments.domain.vo.PaymentIntent;
import com.github.sandokandias.payments.domain.vo.PaymentMethod;
import com.github.sandokandias.payments.interfaces.rest.model.PerformPaymentRequest;
import com.github.sandokandias.payments.interfaces.rest.model.PerformPaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController("/v1/payments")
public class PaymentController {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentProcessManager paymentProcessManager;

    public PaymentController(PaymentProcessManager paymentProcessManager) {
        this.paymentProcessManager = paymentProcessManager;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Callable<CompletionStage<ResponseEntity<?>>> process(@Valid @RequestBody PerformPaymentRequest request) {

        LOG.debug("Request {}", request);

        return () -> {
            LOG.debug("Callable...");

            PaymentCommand.PerformPayment performPayment = PaymentCommand.PerformPayment.commandOf(
                    new CustomerId(request.getCustomerId()),
                    PaymentIntent.valueOf(request.getPaymentIntent()),
                    PaymentMethod.valueOf(request.getPaymentMethod()),
                    request.getTransaction());

            return paymentProcessManager.process(performPayment)
                    .thenApply(acceptOrReject -> acceptOrReject.fold(
                            reject -> ResponseEntity.badRequest().body(reject),
                            accept -> ResponseEntity.accepted().body(new PerformPaymentResponse(accept._1.id, accept._2.name()))
            ));
        };
    }
}
