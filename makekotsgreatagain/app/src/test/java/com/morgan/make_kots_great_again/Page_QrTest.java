package com.morgan.make_kots_great_again;

import org.junit.Test;

import static org.junit.Assert.*;

public class Page_QrTest {

    @Test
    public void isTokenWrong() {
        Page_Qr page_qr = new Page_Qr();

        assertTrue(page_qr.isTokenWrong("Unauthorized"));
        assertFalse(page_qr.isTokenWrong("Successful request"));
    }
}