package com.investasikita.news.client.model;

import java.util.Collection;

public class Investor {

    private static final long serialVersionUID = 8201167419183753403L;

    public Investor() {
    }

    public Client clientEntity;
    public Individu individuEntity;
    public Institution institutionEntity;
    public Collection<ClientUser> clientUserEntities;
    public Collection<ClientGerai> clientGeraiEntities;
    public Collection<BankAccount> bankAccountEntities;

}
