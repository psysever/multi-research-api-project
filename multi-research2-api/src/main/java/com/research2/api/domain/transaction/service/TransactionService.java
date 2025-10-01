package com.research2.api.domain.transaction.service;


import com.research2.api.domain.transaction.dto.req.CreatePaymentHistoryDto;

public interface TransactionService {

    void paymentHistory(CreatePaymentHistoryDto createPaymentHistoryDto);


}
