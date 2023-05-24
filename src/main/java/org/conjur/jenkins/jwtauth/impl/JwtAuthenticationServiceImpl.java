package org.conjur.jenkins.jwtauth.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.conjur.jenkins.configuration.GlobalConjurConfiguration;
import org.conjur.jenkins.jwtauth.JwtAuthenticationService;
import org.json.JSONObject;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;

@Extension
public class JwtAuthenticationServiceImpl extends JwtAuthenticationService {
	private static final Logger LOGGER = Logger.getLogger(JwtAuthenticationServiceImpl.class.getName());

	@Override
	public String getJwkSet() throws HttpRequestMethodNotSupportedException {
		LOGGER.log(Level.FINE, "Getting JwkSet");

		GlobalConjurConfiguration result = GlobalConfiguration.all().get(GlobalConjurConfiguration.class);
		String key = "";
		if (result == null || !result.getEnableJWKS()) {
			LOGGER.log(Level.FINE, "Global Configuration in JWTToken Impl", "result is null");
			throw new HttpRequestMethodNotSupportedException("conjur-jwk-set");
		} else {
			LOGGER.log(Level.FINE, "Global Configuration in JWTToken Impl", result.getDisplayName());

		}

		if (JWTTokenStorage.checkFileExists()) {
			LOGGER.log(Level.INFO, "File Exists");

			if (JWTTokenStorage.checkFileNotEmpty()) {

				LOGGER.log(Level.INFO, "File not empty,Reading public key from file");

				String publicKey = JWTTokenStorage.readFromFile();
				LOGGER.log(Level.INFO, "Public key from file,public key" + publicKey);
				/*if (publicKey.equals("{") || publicKey.isEmpty()) {
					LOGGER.log(Level.INFO, "Calling JwtToken to fetch public key");
					key = JwtToken.getJwkset().toString(4);
				} else {*/
					JSONObject jwks = new JSONObject(publicKey);

					LOGGER.log(Level.INFO, "Public key from file json object" + jwks.toString());
					key = jwks.toString();
				//}

			} else {
				LOGGER.log(Level.INFO, "File empty,calling JWT Token to retrieve public key");
				key = JwtToken.getJwkset().toString(4);
			}
			

		}

		// key = JwtToken.getJwkset().toString(4);
		return key;
	}

	@Override
	public String getIconFileName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "Conjur JWT endpoint";
	}

}
