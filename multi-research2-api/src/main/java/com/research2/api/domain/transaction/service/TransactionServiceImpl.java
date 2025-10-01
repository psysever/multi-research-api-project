package com.research2.api.domain.transaction.service;


import com.research2.api.domain.transaction.dto.req.CreatePaymentHistoryDto;
import com.research2.api.domain.transaction.entity.Payment;
import com.research2.api.domain.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


@Service("TransactionServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;


    //only read
    @Transactional(readOnly = true)
    public Payment findPayment() {
        return transactionRepository.findPayment();
    }


    //Logging: When logs must be preserved even if external transactions are rolled back.
    //Auditing: When audit records must be kept regardless of whether a task fails.
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void paymentHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }

    //Lookup-only method
    @Transactional(propagation = Propagation.SUPPORTS)
    public void paymentSupportsHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }


    //Must be executed within a transaction only
    @Transactional(propagation = Propagation.MANDATORY)
    public void paymentMandatoryHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }


    //Partial rollback required
    @Transactional(propagation = Propagation.NESTED)
    public void paymentNestedHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }


    // Execute after transaction commit (default)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void paymentAfterCommitHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }

    // Execute after transaction rollback
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void paymentAfterRollBackHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }

    // Executed after transaction completion (regardless of commit/rollback)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void paymentAfterCompletionHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }


    // Executed just before transaction commit
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paymentBeforeCommitHistory(CreatePaymentHistoryDto createPaymentHistoryDto) {
        transactionRepository.paymentHistory(createPaymentHistoryDto);
    }

}
