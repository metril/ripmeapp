package com.rarchives.ripme.tst.ripper.rippers;

import java.io.IOException;
import java.net.URL;

import com.rarchives.ripme.ripper.rippers.XvideosRipper;
import org.junit.jupiter.api.Test;

public class XvideosRipperTest extends RippersTest {
    @Test
    public void testXhamsterAlbum1() throws IOException {
        XvideosRipper ripper = new XvideosRipper(new URL("https://www.xvideos.com/video23515878/dee_s_pool_toys"));
        testRipper(ripper);
    }

}
