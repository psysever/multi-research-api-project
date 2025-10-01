package com.research2.api.domain.transaction.repository;


import com.research2.api.domain.transaction.dto.req.CreatePaymentHistoryDto;
import com.research2.api.domain.transaction.entity.Payment;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface TransactionRepository {

    Payment findPayment();

    void paymentHistory(CreatePaymentHistoryDto createPaymentHistoryDto);


}
