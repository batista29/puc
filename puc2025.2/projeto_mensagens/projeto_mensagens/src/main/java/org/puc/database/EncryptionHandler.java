package org.puc.database;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

public class EncryptionHandler {

    private static final String PASS_KEY = "BOBESPONJ@top12344321";

    public static SecretKeySpec getKey(String chave) {
        byte[] hash = chave.getBytes(StandardCharsets.UTF_8);
        byte[] chaveFinal = Arrays.copyOf(hash, 16);
        return new SecretKeySpec(chaveFinal, "AES");
    }

    public static String encrypt(String texto, String chave) throws Exception {
        // Define o algoritmo: AES + modo ECB + padding padrão
        try {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getKey(chave));
        byte[] encrypted = cipher.doFinal(texto.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return "Erro ao criptografar: " + e.getMessage();
        }
    }

    public static String decrypt(String textoCriptografado, String chave) throws Exception {
        try {
        // Mesmo algoritmo usado na criptografia
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

        // Inicializa o modo de descriptografia com a mesma chave
        cipher.init(Cipher.DECRYPT_MODE, getKey(chave));

        // Decodifica o Base64 e descriptografa
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(textoCriptografado));
        return new String(decrypted);
        } catch (Exception e) {
            return "Erro ao descriptografar: " + e.getMessage();
        }
    }

    public static String encryptPass(String data) throws Exception {
        //criptografa senha do usuario
        return encrypt(data, PASS_KEY);
    }
}
