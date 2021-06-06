package com.msc.test1fichier.downloader;

import com.msc.test1fichier.downloader.generic.Generic;
import com.msc.test1fichier.downloader.unfichier.UnFichier;
import java.net.URL;

/**
 *
 * @author Michael
 */
public class DownloaderFactory {

    public enum Hebergeur {
        UN_FICHIER, GENERIC
    }

    private static UnFichier unFichier = new UnFichier();

    private static Generic generic = new Generic();

    public static Downloader getDownloader(Hebergeur h) {
        switch (h) {
            case GENERIC:
                return generic;
            case UN_FICHIER:
                return unFichier;
        }
        return null;
    }

    public static Downloader getAutoDownloader(URL url) {
        if (unFichier.match(url)) {
            return getDownloader(Hebergeur.UN_FICHIER);
        }
        return getDownloader(Hebergeur.GENERIC);
    }

}
