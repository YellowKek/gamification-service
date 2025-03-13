package com.business.money.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PasswordGenerator {
    private final SecureRandom random = new SecureRandom();

    public String generate(){
        int passwordLength = 10;
        return random.ints(48, 123)
                .filter(i -> (i < 58 || i > 64) && (i < 91 || i > 96))
                .limit(passwordLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}