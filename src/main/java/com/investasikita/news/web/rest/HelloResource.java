/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.investasikita.news.web.rest;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rendr
 */
@RestController
@RequestMapping("/api")
public class HelloResource {
    
    @Value("${server.port}")
    private int port;
    
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello(HttpServletRequest request) throws UnknownHostException, SocketException {  
        String ipaddr = "";
        try(final DatagramSocket socket = new DatagramSocket()){
          socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
          ipaddr = socket.getLocalAddress().getHostAddress();
        }
        return new String("http://" + ipaddr + ":" + port);
    }
    
}
