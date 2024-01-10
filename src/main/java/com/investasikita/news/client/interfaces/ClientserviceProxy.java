package com.investasikita.news.client.interfaces;

import com.investasikita.news.client.AuthorizedUserFeignClient;
import com.investasikita.news.client.model.Investor;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@AuthorizedUserFeignClient(name = "clientservice")
public interface ClientserviceProxy {
    @RequestMapping(value = "/api/investors/profile")
    Optional<Investor> getInvestorDetail();
}
