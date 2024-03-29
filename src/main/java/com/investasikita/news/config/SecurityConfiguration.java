package com.investasikita.news.config;

import com.investasikita.news.config.oauth2.OAuth2JwtAccessTokenConverter;
import com.investasikita.news.config.oauth2.OAuth2Properties;
import com.investasikita.news.security.oauth2.OAuth2SignatureVerifierClient;
import com.investasikita.news.security.AuthoritiesConstants;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends ResourceServerConfigurerAdapter {
    private final OAuth2Properties oAuth2Properties;

    public SecurityConfiguration(OAuth2Properties oAuth2Properties) {
        this.oAuth2Properties = oAuth2Properties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()   
            .antMatchers("/api/hello").permitAll()     
            .antMatchers("/api/v1/news/**").permitAll()
//            .antMatchers("/api/v1/news-search").permitAll()
            .antMatchers("/api/v1/headlines/**").permitAll()
            .antMatchers("/api/v1/categories/**").permitAll()
            .antMatchers("/api/v1/_search/**").permitAll()
            .antMatchers("/management/health").permitAll()    
            .antMatchers("/management/info").permitAll()
            .antMatchers(
                    "/management/**",
                    "/api/management/**")
                .hasAnyAuthority(
                    AuthoritiesConstants.MK, 
                    AuthoritiesConstants.MKM, 
                    AuthoritiesConstants.REP, 
                    AuthoritiesConstants.EDT, 
                    AuthoritiesConstants.ADMIN)
            .antMatchers("/api/**").authenticated();
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(OAuth2SignatureVerifierClient signatureVerifierClient) {
        return new OAuth2JwtAccessTokenConverter(oAuth2Properties, signatureVerifierClient);
    }

    @Bean
	@Qualifier("loadBalancedRestTemplate")
    public RestTemplate loadBalancedRestTemplate(RestTemplateCustomizer customizer) {
        RestTemplate restTemplate = new RestTemplate();
        customizer.customize(restTemplate);
        return restTemplate;
    }

    @Bean
    @Qualifier("vanillaRestTemplate")
    public RestTemplate vanillaRestTemplate() {
        return new RestTemplate();
    }
}
