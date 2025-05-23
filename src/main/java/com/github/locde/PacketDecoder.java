package com.github.locde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketDecoder {

    private static final ObjectMapper objMapper = new ObjectMapper();

    @SneakyThrows
    public static Product decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN);

        byte bMagic = buffer.get();
        if (bMagic != 0x13)
            throw new IllegalArgumentException();

        byte bSrc = buffer.get();
        long pktId = buffer.getLong();
        int wLen = buffer.getInt();
        short wCrc16 = buffer.getShort();

        short expectedCrc = CRC16.calcCrc(buffer.array(), 0, 14);
        if (wCrc16 != expectedCrc)
            throw new IllegalArgumentException();

        int cType = buffer.getInt();
        int bUserId = buffer.getInt();

        int msgSize = wLen - 8;
        byte[] msgBytes = new byte[msgSize];
        buffer.get(msgBytes, 0, msgSize);

        short w2Crc16 = buffer.getShort(bytes.length - 2);
        short expectedCrc2 = CRC16.calcCrc(buffer.array(), 16, wLen);
        if (w2Crc16 != expectedCrc2)
            throw new IllegalArgumentException();

        byte[] decryptedBytes = CryptoService.decrypt(msgBytes);
        return objMapper.readValue(decryptedBytes, Product.class);
    }

}
