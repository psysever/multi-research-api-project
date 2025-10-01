package com.research2.api.domain.cipher.service;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.RSADecrypter;
import com.research2.api.domain.cipher.config.RSAKeyProvider;
import com.research2.api.domain.cipher.utills.AesGcmAtRestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.interfaces.RSAPrivateKey;
import java.text.ParseException;

@Service("CipherServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class CipherServiceImpl implements CipherService {
    private final RSAKeyProvider rsaKeyProvider;
    private final AesGcmAtRestUtil aesGcm;

    // payload Encryption and decryption
    // Compact JWE (alg: RSA-OAEP-256, enc: A256GCM) -> Plain
    public String decryptFromClientJwe(String compactJwe) {
        try {
            if (compactJwe != null || compactJwe.trim().isEmpty()) {
                JWEObject jwe = JWEObject.parse(compactJwe);
                RSAPrivateKey priv = rsaKeyProvider.getPrivateKey();
                jwe.decrypt(new RSADecrypter(priv));
                return jwe.getPayload().toString();
            } else {
                return "";
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid JWE format", e);
        } catch (JOSEException e) {
            throw new IllegalArgumentException("JWE decryption failed", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error during JWE decryption", e);
        }
    }

//decryptFromClientJwe example

//@Transactional
//public UserInfoResDto createWebUser(CreateUserDto createUserDto) {
//    try {
//        String decryptedMbName = cipherService.decryptFromClientJwe(createUserDto.getMbName()).trim();
//
//
//        if (decryptedMbName.isEmpty()) {
//            throw new IllegalArgumentException("Member name cannot be empty");
//        }
//
//        String encryptedMbName = cipherService.atRestEncrypt(decryptedMbName);
//        createUserDto.setMbName(encryptedMbName);
//
//        return userRepository.createUser(createUserDto);
//    } catch (Exception e) {
//        logger.error("User creation failed", e);
//        throw e; // 트랜잭션 롤백
//    }
//}

    //AES ENCRYPT
    public String atRestEncrypt(String plain) {
        if (plain == null || plain.isEmpty()) return plain; // "" 유지
        try {
            return aesGcm.encryptToToken(plain);
        } catch (Exception e) {
            // 평문 저장을 피하려면 예외를 던져 롤백하는 게 더 안전
            throw new RuntimeException("atRestEncrypt failed", e);
        }
    }


    //AES DECRYPT
    public String safeDecrypt(String enc) {
        if (enc == null || enc.isBlank()) return "";
        try {
            return atRestDecrypt(enc);
        } catch (Exception e) {
            // 로깅 정도만 하고 null 반환 (엑셀 깨짐 방지)
            // log.warn("Decrypt fail, value: {}", enc, e);
            return null;
        }
    }


    //safeDecrypt Example

//    public UserInfoResDto findOneUserInfo(int mbUniqueKey) {
//        UserInfoResDto userInfo = userRepository.findOneUserInfo(mbUniqueKey);
//        if (userInfo != null) {
//            userInfo.setMbEmail(cryptoHelper.safeDecrypt(userInfo.getMbEmail()));
//            userInfo.setMbName(cryptoHelper.safeDecrypt(userInfo.getMbName()));
//            userInfo.setMbSex(cryptoHelper.safeDecrypt(userInfo.getMbSex()));
//            userInfo.setMbBirth(cryptoHelper.safeDecrypt(userInfo.getMbBirth()));
//            userInfo.setMbHp(cryptoHelper.safeDecrypt(userInfo.getMbHp()));
//        }
//
//        return userInfo;
//    }

    public String atRestDecrypt(String maybe) {
        if (maybe == null || maybe.isEmpty()) return maybe; // "" case
        try {
            return aesGcm.decryptFromToken(maybe);
        } catch (Exception e) {
            return maybe;
        }
    }


}

