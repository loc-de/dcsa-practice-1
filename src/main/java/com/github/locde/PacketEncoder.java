package com.github.locde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketEncoder {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static final ObjectMapper objMapper = new ObjectMapper();

    @SneakyThrows
    public static byte[] encode(Product msg) {
        byte[] bytes = objMapper.writeValueAsBytes(msg);
        byte[] encryptedBytes = CryptoService.encrypt(bytes);
        int msgSize = encryptedBytes.length + 4 + 4;
        int size = 1 + 1 + 8 + 4 + 2 + msgSize + 2;

        ByteBuffer buffer = ByteBuffer.allocate(size).order(ByteOrder.BIG_ENDIAN);
        buffer.put((byte)0x13)
                .put((byte)1)
                .putLong(2)
                .putInt(msgSize)
                .putShort(CRC16.calcCrc(buffer.array(), 0, 14))
                .putInt(3)
                .putInt(4)
                .put(encryptedBytes)
                .putShort(CRC16.calcCrc(buffer.array(), 16, msgSize));

        return buffer.array();
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

}
