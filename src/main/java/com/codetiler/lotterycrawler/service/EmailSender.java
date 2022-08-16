package com.codetiler.lotterycrawler.service;

public interface EmailSender {

    void send(String to,String subject,String content);
}
