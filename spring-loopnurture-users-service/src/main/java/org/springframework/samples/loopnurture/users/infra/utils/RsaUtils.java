package org.springframework.samples.loopnurture.users.infra.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA加解密工具类
 */
@Slf4j
@Component
public class RsaUtils {
    private final KeyPair keyPair;

    public RsaUtils() {
        this.keyPair = generateKeyPair();
    }

    /**
     * 生成密钥对
     */
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            return generator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate RSA key pair", e);
        }
    }

    /**
     * 获取公钥（Base64编码）
     */
    public String getPublicKey() {
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }

    /**
     * 使用私钥解密
     */
    public String decrypt(String encryptedData) {
        try {
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        } catch (Exception e) {
            log.error("RSA解密失败", e);
            throw new RuntimeException("密码解密失败");
        }
    }

    /**
     * 使用公钥加密（提供给前端使用）
     * 注意：实际项目中，前端会使用自己的加密实现，这个方法主要用于测试
     */
    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.error("RSA加密失败", e);
            throw new RuntimeException("密码加密失败");
        }
    }
} 