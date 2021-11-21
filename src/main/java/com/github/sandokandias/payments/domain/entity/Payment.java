package com.github.sandokandias.payments.domain.entity;

import com.github.sandokandias.payments.domain.command.PaymentCommand;
import com.github.sandokandias.payments.domain.command.handler.AuthorizePaymentHandler;
import com.github.sandokandias.payments.domain.command.handler.ConfirmPaymentHandler;
import com.github.sandokandias.payments.domain.command.handler.PerformPaymentHandler;
import com.github.sandokandias.payments.domain.shared.AggregateRoot;
import com.github.sandokandias.payments.domain.vo.PaymentId;
import org.springframework.context.ApplicationContext;

public class Payment extends AggregateRoot<Payment, PaymentId> {

    public Payment(ApplicationContext applicationContext) {
        super(applicationContext, new PaymentId());
    }

    public Payment(ApplicationContext applicationContext, PaymentId paymentId) {
        super(applicationContext, paymentId);
    }

    @Override
    public boolean sameIdentityAs(Payment other) {
        return other != null && entityId.sameValueAs(other.entityId);
    }

    @Override
    public PaymentId id() {
        return entityId;
    }

    @Override
    protected AggregateRootBehavior initialBehavior() {
        AggregateRootBehaviorBuilder behaviorBuilder = new AggregateRootBehaviorBuilder();
        behaviorBuilder.setCommandHandler(PaymentCommand.PerformPayment.class, getHandler(PerformPaymentHandler.class));
        behaviorBuilder.setCommandHandler(PaymentCommand.AuthorizePayment.class, getHandler(AuthorizePaymentHandler.class));
        behaviorBuilder.setCommandHandler(PaymentCommand.ConfirmPayment.class, getHandler(ConfirmPaymentHandler.class));
        return behaviorBuilder.build();
    }
}
