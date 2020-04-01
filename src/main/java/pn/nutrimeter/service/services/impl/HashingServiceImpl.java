package pn.nutrimeter.service.services.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import pn.nutrimeter.service.services.api.HashingService;

@Service
public class HashingServiceImpl implements HashingService {

    @Override
    public String hash(String password) {
        return DigestUtils.sha256Hex(password);
    }
}
