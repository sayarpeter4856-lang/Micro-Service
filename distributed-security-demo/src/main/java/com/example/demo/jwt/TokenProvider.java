package com.example.demo.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
@Component
public class TokenProvider {
	
	public static final String SECRET_KEY="my-256-bit-extremely-secret-key-for-jwt-singing-key";
	
	public String createToken(String username)throws JOSEException{
		JWTClaimsSet claimSet= new JWTClaimsSet.Builder()
				.subject(username)
				.issuer("my-app")
				.issueTime(new Date())
				.expirationTime(new Date(System.currentTimeMillis()
						+ 1000 * 60 * 60 * 24))
				.build();
		SignedJWT signedJWT=new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), 
				claimSet);
		JWSSigner signer=new MACSigner(SECRET_KEY);
		signedJWT.sign(signer);
		return signedJWT.serialize();
	}

}
