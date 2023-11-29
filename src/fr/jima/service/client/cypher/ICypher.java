package fr.jima.service.client.cypher;

public interface ICypher {

    public String encode(String data);

    public String decode(String data);

    public String toString();
}
