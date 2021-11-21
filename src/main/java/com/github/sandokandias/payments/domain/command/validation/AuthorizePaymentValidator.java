package com.github.sandokandias.payments.domain.command.validation;

import com.github.sandokandias.payments.domain.command.PaymentCommand;
import com.github.sandokandias.payments.domain.shared.CommandFailure;
import com.github.sandokandias.payments.domain.shared.CommandValidation;
import io.vavr.control.Either;
import org.springframework.stereotype.Component;

@Component
public class AuthorizePaymentValidator implements CommandValidation<PaymentCommand.AuthorizePayment> {

    @Override
    public Either<CommandFailure, PaymentCommand.AuthorizePayment> acceptOrReject(PaymentCommand.AuthorizePayment command) {
        return Either.right(command);
    }
}
