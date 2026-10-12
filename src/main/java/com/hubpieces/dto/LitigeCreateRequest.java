package com.hubpieces.dto;

public class LitigeCreateRequest {
    private Long transactionId;
    private String raison;

    public LitigeCreateRequest() {}

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }
    public String getRaison() { return raison; }
    public void setRaison(String raison) { this.raison = raison; }
}
