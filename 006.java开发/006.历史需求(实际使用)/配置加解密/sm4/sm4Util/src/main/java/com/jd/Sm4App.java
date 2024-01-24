package com.jd;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class Sm4App {
    private static final Scanner sc = new Scanner(System.in);
    private static ArrayList<HashMap<String, String>> res = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String k;
        String v;
        System.out.println("======sm4加密小程序启动======");
        System.out.println("是否自动生成key和iv?n/y");
        if (!sc.next().toUpperCase(Locale.ROOT).equals("Y")) {
            System.out.println("请输入您的自定义key(& >= 128bit)");
            k = sc.next();
            System.out.println("请输入您的自定义iv(& >= 128bit)");
            v = sc.next();
        } else {
            k = hex();
            v = hex();
        }

        Key sm4key = new SecretKeySpec(Hex.decode(k), "SM4");
        HashMap<String, String> keyMap = new HashMap<>();
        keyMap.put("sm4keyHex", k);
        res.add(keyMap);

        byte[] sm4iv = Hex.decode(v);
        HashMap<String, String> ivMap = new HashMap<>();
        ivMap.put("sm4ivHex", v);
        res.add(ivMap);

        do {
            final HashMap<String, String> map = new HashMap<>();
            System.out.print("请输入密码以加密:");
            String next = sc.next();
            String encryptPassWord = encrypt(next, sm4key, sm4iv);
            System.out.println(encryptPassWord);
            map.put(next, encryptPassWord);
            res.add(map);
            System.out.print("是否退出?n/y:");
        } while (!sc.next().toUpperCase(Locale.ROOT).equals("Y"));
        System.out.println("======sm4加密小程序结束======");
        System.out.println("======配置保存至当前目录,请输入 cat ../config 查看======");

        try (final BufferedWriter bw = new BufferedWriter(new FileWriter("../config", false))) {
            for (HashMap<String, String> re : res) {
                System.out.println(re);
                for (String s : re.keySet()) {
                    bw.write(s + "=" + re.get(s));
                    bw.newLine();
                    bw.flush();
                }
            }
        }

    }

    private static String encrypt(String input, Key key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(input.getBytes());
        return Hex.toHexString(encrypted);
    }

    private static String decrypt(String input, Key key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance("SM4/CBC/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
        byte[] decrypted = cipher.doFinal(Hex.decode(input));
        return new String(decrypted);
    }

    private static String hex() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16]; // 16 bytes * 8 = 128 bits
        random.nextBytes(bytes);

        BigInteger bigInteger = new BigInteger(1, bytes);
        String hexString = bigInteger.toString(16); // Convert to hexadecimal

        // Pad with 0s to ensure the string has 32 characters (16 bytes * 2)
        while (hexString.length() < 32) {
            hexString = "0" + hexString;
        }

        return hexString;
    }
}
