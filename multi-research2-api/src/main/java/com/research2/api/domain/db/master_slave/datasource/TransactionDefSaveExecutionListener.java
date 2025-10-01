package com.research2.api.domain.db.master_slave.datasource;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionExecution;
import org.springframework.transaction.TransactionExecutionListener;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionDefSaveExecutionListener implements TransactionExecutionListener {

    public void beforeBegin(@org.jetbrains.annotations.NotNull @NotNull TransactionExecution transaction) {
        DefaultTransactionStatus defaultTxStatus = (DefaultTransactionStatus) transaction;
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(defaultTxStatus.isReadOnly());
        TransactionSynchronizationManager.setActualTransactionActive(true);
    }

}
