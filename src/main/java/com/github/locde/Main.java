package com.github.locde;

public class Main {

    public static void main(String[] args) {
        Product msg = new Product("smth", 100, 5);

        byte[] out = PacketEncoder.encode(msg);
        System.out.println(PacketEncoder.bytesToHex(out));

        Product decoded = PacketDecoder.decode(out);
        System.out.println(decoded);
    }

}
