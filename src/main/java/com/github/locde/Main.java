package com.github.locde;

public class Main {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args) {
        Product msg = new Product("name", 2, 3);

        byte[] out = PacketEncoder.encode(msg);
        System.out.println(bytesToHex(out));

        Product decoded = PacketDecoder.decode(out);
        System.out.println(decoded);
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
