package br.com.carometro.security;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Criptografia {
	//Realiza a decodificação da senha de String para md5
	public static String md5(String senha) throws NoSuchAlgorithmException {
		
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		BigInteger hash = new BigInteger(1, messageDigest.digest(senha.getBytes()));
		return hash.toString(16);
	}
}
