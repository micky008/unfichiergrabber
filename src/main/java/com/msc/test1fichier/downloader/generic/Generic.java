package com.msc.test1fichier.downloader.generic;

import com.msc.test1fichier.downloader.Downloader;
import com.msc.test1fichier.http.HttpClient;
import com.msc.test1fichier.http.HttpClient.Header;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michael
 */
public class Generic implements Downloader {

    private static List<Header> emptyList = new ArrayList<>(0);

    @Override
    public boolean init(String login, String password) {
        return true;
    }

    @Override
    public InputStream download(URL url) {
        return HttpClient.download(url, emptyList);
    }

    @Override
    public boolean match(URL url) {
        return true;
    }

}
