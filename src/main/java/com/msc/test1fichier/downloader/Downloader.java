package com.msc.test1fichier.downloader;

import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author Michael
 */
public interface Downloader {

    public boolean init(String login, String password);

    public InputStream download(URL url);
    
    public boolean match(URL url);
    

}
