package fr.jima.service.client.cypher;

public class ClearText implements ICypher {

    @Override
    public String encode(String data) {
        return data;
    }

    @Override
    public String decode(String data) {
        return data;
    }

    @Override
    public String toString() {
        return "Clear text";
    }

}