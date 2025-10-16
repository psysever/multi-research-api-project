package com.research2.api.domain.db.master_slave.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {

        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();


        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (!isTransactionActive) {
            return "master";
        }
        return isReadOnly ? "slave" : "master";
    }
}


