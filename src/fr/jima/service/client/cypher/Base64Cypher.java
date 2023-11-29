package fr.jima.service.client.cypher;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

//import javax.xml.bind.DatatypeConverter;

public class Base64Cypher implements ICypher {

    public static String CHARSET = "UTF-8";

    @Override
    public String encode(String data) {
        // Java 8
        return new String(Base64.getEncoder().encode(data.getBytes()));
        // Java 7
//		try {
//			return DatatypeConverter.printBase64Binary(data.getBytes(CHARSET));
//		}
//		catch (UnsupportedEncodingException e) {
//			return DatatypeConverter.printBase64Binary(data.getBytes());
//		}
    }

    @Override
    public String decode(String data) {
        // Java 8
        String s = new String(Base64.getDecoder().decode(data.getBytes()));
        return s;
        // Java 7
//		try {
//			return new String(DatatypeConverter.parseBase64Binary(data), CHARSET);
//		}
//		catch (UnsupportedEncodingException e) {
//			return new String(DatatypeConverter.parseBase64Binary(data));
//		}
    }

    @Override
    public String toString() {
        return "Base 64";
    }

}