package org.conjur.jenkins.jwtauth.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicKeyToken implements Serializable{
	
	private static final long serializeVersionUID =1;
	private static final Logger LOGGER = Logger.getLogger(JWTTokenStorage.class.getName());
	
	
	private String publicKey;

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	
	private void writeObject(ObjectOutputStream oos) throws IOException
	{
		 LOGGER.log(Level.FINE, "writeObject");
		
		oos.defaultWriteObject();
		oos.writeObject(getPublicKey());
		
	}
	
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException,IOException
	{
		 LOGGER.log(Level.FINE, "readObject");
		ois.defaultReadObject();
		String ret_publicKey = (String) ois.readObject();
		this.setPublicKey(ret_publicKey);
		
	}
	

}
