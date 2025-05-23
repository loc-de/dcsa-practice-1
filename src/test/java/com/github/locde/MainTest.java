package com.github.locde;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void decodeEmptyPacket_shouldThrowException() {
        byte[] empty = new byte[0];
        assertThrows(Exception.class, () -> PacketDecoder.decode(empty));
    }

    @Test
    void givenProduct_shouldEncodeToHexString() {
        Product msg = new Product("smth", 100, 5);
        String expected = "130100000000000000020000003840680000000300000004352C61842BCD25A74C1B1EA73B329A505F3FC0F37055B6327DC11C3F4C1926139EA981BD68766494C0F19176ECDB2B3DC9D9";

        byte[] encoded = PacketEncoder.encode(msg);
        assertEquals(expected, PacketEncoder.bytesToHex(encoded));
    }

    @Test
    void givenProduct_shouldDecodeProduct() {
        Product expected = new Product("smth", 100, 5);
        byte[] encoded = new byte[] {19, 1, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 56, 64, 104, 0, 0, 0, 3, 0, 0, 0, 4, 53, 44, 97, -124, 43, -51, 37, -89, 76, 27, 30, -89, 59, 50, -102, 80, 95, 63, -64, -13, 112, 85, -74, 50, 125, -63, 28, 63, 76, 25, 38, 19, -98, -87, -127, -67, 104, 118, 100, -108, -64, -15, -111, 118, -20, -37, 43, 61, -55, -39};

        Product decoded = PacketDecoder.decode(encoded);
        assertEquals(expected, decoded);
    }

    @Test
    void givenProduct_shouldEncodeAndDecodeCorrectly() {
        Product input = new Product("smth", 100, 5);
        byte[] packet = PacketEncoder.encode(input);
        Product output = PacketDecoder.decode(packet);

        assertEquals(input, output);
    }

    @Test
    void givenProduct_shouldThrowException() {
        byte[] bytes = new byte[] {19, 1, 0, 0, 0, 2, 0, 0, 0, 56, 64, 104, 0, 0, 0, 3, 0, 0, 0, 4, 53, 44, 97, -124, 43, -51, 37, -89, 76, 27, 30, -89, 59, 50, -102, 80, 95, 63, -64, -13, 112, 85, -74, 50, 125, -63, 28, 63, 76, 25, 38, 19, -98, -87, -127, -67, 104, 118, 100, -108, -64, -15, -111, 118, -20, -37, 43, 61, -55, -39};

        assertThrows(IllegalArgumentException.class, () -> PacketDecoder.decode(bytes));
    }

}
