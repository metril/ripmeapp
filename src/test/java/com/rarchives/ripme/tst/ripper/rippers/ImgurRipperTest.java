package com.rarchives.ripme.tst.ripper.rippers;

import com.rarchives.ripme.ripper.rippers.ImgurRipper;
import com.rarchives.ripme.ripper.rippers.ImgurRipper.ImgurAlbum;
import com.rarchives.ripme.utils.RipUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ImgurRipperTest extends RippersTest {
    @Test
    public void testImgurURLFailures() throws IOException, URISyntaxException {
        List<URL> failURLs = new ArrayList<>();
        // Imgur urls that should not work
        failURLs.add(new URI("http://imgur.com").toURL());
        failURLs.add(new URI("http://imgur.com/").toURL());
        failURLs.add(new URI("http://i.imgur.com").toURL());
        failURLs.add(new URI("http://i.imgur.com/").toURL());
        failURLs.add(new URI("http://imgur.com/image.jpg").toURL());
        failURLs.add(new URI("http://i.imgur.com/image.jpg").toURL());
        for (URL url : failURLs) {
            try {
                new ImgurRipper(url);
                Assertions.fail("Instantiated ripper for URL that should not work: " + url);
            } catch (Exception e) {
                // Expected
            }
        }
    }

    @Test
    @Tag("flaky")
    public void testImgurAlbums() throws IOException, URISyntaxException {
        List<URL> contentURLs = new ArrayList<>();
        // URLs that should return more than 1 image
        //contentURLs.add(new URI("http://imgur.com/a/dS9OQ#0").toURL()); // Horizontal layout
        //contentURLs.add(new URI("http://imgur.com/a/YpsW9#0").toURL()); // Grid layout
        contentURLs.add(new URI("http://imgur.com/a/WxG6f/layout/vertical#0").toURL());
        contentURLs.add(new URI("http://imgur.com/a/WxG6f/layout/horizontal#0").toURL());
        contentURLs.add(new URI("http://imgur.com/a/WxG6f/layout/grid#0").toURL());
        contentURLs.add(new URI("http://imgur.com/gallery/FmP2o").toURL()); // Gallery URL
        // Imgur seems not to support URLs with lists of images anymore.
        // contentURLs.add(new
        // URL("http://imgur.com/758qD43,C6iVJex,bP7flAu,J3l85Ri,1U7fhu5,MbuAUCM,JF4vOXQ"));
        // Sometimes hangs up
        // contentURLs.add(new URI("http://imgur.com/r/nsfw_oc/top/all").toURL());
        // contentURLs.add(new URI("http://imgur.com/a/bXQpH").toURL()); // Album with
        // titles/descriptions
        for (URL url : contentURLs) {
            ImgurRipper ripper = new ImgurRipper(url);
            testRipper(ripper);
        }
    }

    @Test
    @Disabled("test or ripper broken")
    public void testImgurSingleImage() throws IOException, URISyntaxException {
        List<URL> contentURLs = new ArrayList<>();
        contentURLs.add(new URI("http://imgur.com/qbfcLyG").toURL()); // Single image URL
        contentURLs.add(new URI("https://imgur.com/KexUO").toURL()); // Single image URL
        for (URL url : contentURLs) {
            ImgurRipper ripper = new ImgurRipper(url);
            testRipper(ripper);
        }
    }

    @Test
    public void testImgurAlbumWithMoreThan20Pictures() throws IOException, URISyntaxException {
        ImgurAlbum album = ImgurRipper.getImgurAlbum(new URI("http://imgur.com/a/HUMsq").toURL());
        Assertions.assertTrue(album.images.size() >= 20,
                "Failed to find 20 files from " + album.url.toExternalForm() + ", only got " + album.images.size());
    }

    @Test
    public void testImgurAlbumWithMoreThan100Pictures() throws IOException, URISyntaxException {
        ImgurAlbum album = ImgurRipper.getImgurAlbum(new URI("https://imgur.com/a/HX3JSrD").toURL());
        Assertions.assertTrue(album.images.size() >= 100,
                "Failed to find 100 files from " + album.url.toExternalForm() + ", only got " + album.images.size());
    }

    @Test
    public void testImgurVideoFromGetFilesFromURL() throws Exception {
        List<URL> urls = RipUtils.getFilesFromURL(new URI("https://i.imgur.com/4TtwxRN.gifv").toURL());
        Assertions.assertEquals("https://i.imgur.com/4TtwxRN.mp4", urls.get(0).toExternalForm());
    }

    /*
     * // Imgur seems to be really flaky with this huge album, or the album was
     * removed or something. // Navigating to this link results in an
     * "over capacity" warning on the page. // I wonder if our testing automation is
     * what is putting this album over capacity? // See issue #376. public void
     * testImgurAlbumWithMoreThan1000Pictures() throws IOException { ImgurAlbum
     * album = ImgurRipper.getImgurAlbum(new URI("http://imgur.com/a/vsuh5").toURL());
     * assertTrue("Failed to find 1000 files from " + album.url.toExternalForm() +
     * ", only got " + album.images.size(), album.images.size() >= 1000); }
     */
}
