package com.github.locde;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PacketEncoder {

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

}
