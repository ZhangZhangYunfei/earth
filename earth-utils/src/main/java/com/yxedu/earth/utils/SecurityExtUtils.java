package com.yxedu.earth.utils;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

@Slf4j
public class SecurityExtUtils {
  private static final String ENCODING = "UTF-8";
  private static final String ALGO_DIGEST = "SHA-256";
  private static final String ALGO_SALT = "SHA1PRNG";
  private static final String ALGO_PBE = "PBEWithSHA256And256BitAES-CBC-BC";
  private static final int ITERATIONS = 20;
  private static final Hex HEX = new Hex(ENCODING);

  /**
   * Digests the merchant password for further authenticating on merchant related operations.
   *
   * @param password the password of merchant
   * @return the hex encoded hash string
   * @throws RuntimeException if failed to digest
   */
  public static String digest(String password) {
    try {
      return Hex.encodeHexString(
          SecurityUtils.digest(ALGO_DIGEST, password.getBytes(ENCODING)));
    } catch (UnsupportedEncodingException | SecurityException err) {
      log.error("Failed to calculate the hash code of merchant password!", err);
      throw new RuntimeException("Error calculating the hash code of merchant password!", err);
    }
  }

  /**
   * Digests the merchant password for further authenticating on merchant related operations.
   *
   * @param password the password of merchant
   * @param salt     the salt of merchant
   * @return the hex encoded hash string
   * @throws RuntimeException if failed to digest
   */
  public static String digest(String password, String salt) {
    try {
      return Hex.encodeHexString(
          SecurityUtils.digest(ALGO_DIGEST, password.getBytes(ENCODING),
              salt.getBytes(ENCODING)));
    } catch (UnsupportedEncodingException | SecurityException err) {
      log.error("Failed to calculate the hash code of merchant password!", err);
      throw new RuntimeException("Error calculating the hash code of merchant password!", err);
    }
  }

  /**
   * Generates a salt for further encrypting & decrypting on sensitive data of merchant.
   *
   * @return the hex encoded salt string
   * @throws RuntimeException if failed to generate
   */
  public static String generateSalt() {
    try {
      return Hex.encodeHexString(SecurityUtils.generateSalt(ALGO_SALT));
    } catch (SecurityException err) {
      log.error("Failed to generate the salt for merchant data encryption!", err);
      throw new RuntimeException("Error generating the salt for merchant data encryption!", err);
    }
  }

  /**
   * Encrypts the sensitive content with the specified password and salt.
   *
   * @param content  the sensitive content to be encrypted
   * @param password the password of merchant
   * @param salt     the HEX encoded salt string of merchant
   * @return a HEX encoded string of encrypted content
   * @throws RuntimeException if failed to encrypt
   */
  public static String encrypt(String content, String password, String salt) {
    if (Strings.isNullOrEmpty(content)) {
      return StringUtils.EMPTY;
    }

    try {
      byte[] saltBytes = Hex.decodeHex(salt.toCharArray());
      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
      PBEParameterSpec parameterSpec = new PBEParameterSpec(saltBytes, ITERATIONS);
      SecretKey key = SecurityUtils.generateSecretKey(ALGO_PBE, keySpec);

      return Hex.encodeHexString(
          SecurityUtils.encrypt(ALGO_PBE, parameterSpec, key, content.getBytes(ENCODING))
      );
    } catch (SecurityException | UnsupportedEncodingException | DecoderException err) {
      log.error("Failed to encrypt the sensitive content of merchant!", err);
      throw new RuntimeException("Error encrypting the sensitive content of merchant!", err);
    }
  }

  /**
   * Decrypts the sensitive content with the specified password and salt.
   *
   * @param content  the encrypted sensitive content
   * @param password the password of merchant
   * @param salt     the HEX encoded salt string of merchant
   * @return a HEX encoded string of encrypted content
   * @throws RuntimeException if failed to encrypt
   */
  public static String decrypt(String content, String password, String salt) {
    if (Strings.isNullOrEmpty(content)) {
      return StringUtils.EMPTY;
    }

    try {
      byte[] saltBytes = Hex.decodeHex(salt.toCharArray());
      PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
      PBEParameterSpec parameterSpec = new PBEParameterSpec(saltBytes, ITERATIONS);
      SecretKey key = SecurityUtils.generateSecretKey(ALGO_PBE, keySpec);

      return new String(
          SecurityUtils.decrypt(
              ALGO_PBE,
              parameterSpec,
              key,
              HEX.decode(content.getBytes(ENCODING))),
          ENCODING
      );
    } catch (DecoderException | UnsupportedEncodingException | SecurityException err) {
      log.error("Failed to decrypt the sensitive content of merchant!", err);
      throw new RuntimeException("Error decrypting the sensitive content of merchant!", err);
    }
  }
}
