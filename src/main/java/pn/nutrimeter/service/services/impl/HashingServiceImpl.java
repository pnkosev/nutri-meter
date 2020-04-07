package pn.nutrimeter.service.services.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pn.nutrimeter.service.services.api.HashingService;

@Service
public class HashingServiceImpl implements HashingService {

    private final PasswordEncoder passwordEncoder;

    public HashingServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String hash(String str) {
        return this.passwordEncoder.encode(str);
    }
}
