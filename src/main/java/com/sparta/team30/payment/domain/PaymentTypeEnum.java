package com.sparta.team30.payment.domain;

public enum PaymentTypeEnum {
    PENDING, //결제중
    COMPLETED, //결제 완료
    FAILED, //결제 실패
    CANCELLED, //결제 취소
}
