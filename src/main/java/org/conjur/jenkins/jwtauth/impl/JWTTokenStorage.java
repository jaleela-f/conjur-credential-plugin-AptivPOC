package org.conjur.jenkins.jwtauth.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.jenkins.cli.shaded.org.apache.commons.io.output.FileWriterWithEncoding;

public class JWTTokenStorage implements Serializable {

	private static final long serializeVersionUID = 1;
	private static final Logger LOGGER = Logger.getLogger(JWTTokenStorage.class.getName());

	String pkToken;

	private final static String homepath = System.getProperty("user.home") + "/secrets/conjur.key";
	private final static File keyFile = new File(homepath);

	public String getPkToken() {
		return pkToken;
	}

	public void setPkToken(String pkToken) {
		this.pkToken = pkToken;
	}

	@SuppressWarnings("deprecation")
	protected static void writeToFile(String jwks) {
		LOGGER.log(Level.FINE, "Calling writeTo File");
		LOGGER.log(Level.FINE, "Public Key>>>>" + jwks);

		LOGGER.log(Level.FINE, "User Home>>>>" + homepath);
		try {
			LOGGER.log(Level.FINE, "Entering try block");

			if (!keyFile.exists()) {

				if (keyFile.createNewFile()) {
					LOGGER.log(Level.FINE, "File created");

				} else {
					LOGGER.log(Level.FINE, "File already exisits");
				}
			}
			JsonParser parser = new JsonParser();
			JsonObject json = parser.parse(jwks).getAsJsonObject();

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonObj = gson.toJson(json);
			LOGGER.log(Level.FINE, "JSON format>>" + jsonObj);

			// if (!jsonObj.equals("{\\n\\\"keys\\\":[]\\n}")) {
			LOGGER.log(Level.FINE, "JSON format full>>" + jsonObj);
			FileWriterWithEncoding fileWriter = new FileWriterWithEncoding(homepath, "UTF-8");

			fileWriter.write(jsonObj);

			fileWriter.close();
			// }

		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.log(Level.INFO, e.getMessage(), e);

		}

	}

	protected static String readFromFile() {
		LOGGER.log(Level.FINE, "Start reading file");
		String homepath = System.getProperty("user.home") + "/secrets/conjur.key";
		LOGGER.log(Level.FINE, "File Path" + homepath);
		StringBuilder key = new StringBuilder();
		String charSet = "UTF-8";

		try {

			BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(homepath), charSet));
			String line;
			while ((line = buff.readLine()) != null) {
				key.append(line);
			}

			LOGGER.log(Level.FINE, "Key read from file" + key);

			buff.close();

		} catch (IOException ex) {
			LOGGER.log(Level.INFO, ex.getMessage(), ex);
		}

		return key.toString();
	}

	protected static boolean checkFileExists() {
		if (keyFile.exists()) {
			return true;
		}
		return false;
	}

	protected static boolean checkFileNotEmpty() {
		if (keyFile.length() == 0) {

			return false;
		}
		return true;
	}

}
