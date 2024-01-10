package com.investasikita.news.client.model;

import java.time.Instant;

/**
 * A DTO for the Client entity.
 */
public class Client {

    public Long id;

    public String cif;

    public String sid;

    public String referral;

    public String clientType;

    public Boolean isActive;

    public Instant createdAt;

    public String createdBy;

    public Long individuId;

    public Long institutionId;

}
