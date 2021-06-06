package com.msc.test1fichier;

import com.google.gson.Gson;
import com.msc.test1fichier.downloader.Downloader;
import com.msc.test1fichier.downloader.DownloaderFactory;
import com.msc.test1fichier.entity.Config;
import com.msc.test1fichier.entity.Film;
import com.msc.test1fichier.entity.Serie;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class NewMain {

    private void download(InputStream is, File destFile) throws IOException {
        byte buf[] = new byte[16384]; //2^14
        FileOutputStream fos = new FileOutputStream(destFile);
        int lu = 0;
        while ((lu = is.read(buf)) > -1) {
            fos.write(buf, 0, lu);
        }
        fos.flush();
        fos.close();
        is.close();
    }

    public void downloadFilm(Film film, File destFolder, Downloader unFichier) {

        if (film.getDestFolder() != null) {
            destFolder = film.getDestFolder();
            destFolder.mkdirs();
        }

        File destFilm = new File(destFolder, film.getNom());
        try {
            destFilm.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }

        InputStream is = unFichier.download(film.getLien());
        try {
            download(is, destFilm);
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void downloadSerie(Serie serie, Config config) {
        int nbDownload = 0;
        Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "j'attaque la serie: " + serie.getNom());
        File serieDestFolder = new File(config.getDestFolder(), serie.getNom());
        for (Map.Entry<String, List<URL>> kv : serie.getSaisons().entrySet()) {
            Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "j'attaque la saison: " + kv.getKey() + "(" + kv.getValue().size() + " episodes)");
            File ultimateDest = new File(serieDestFolder, kv.getKey());
            ultimateDest.mkdirs();
            nbDownload = 0;
            Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "Download en cours");
            for (URL lien : kv.getValue()) {
                Downloader dl = DownloaderFactory.getAutoDownloader(lien);
                dl.init(config.getLogin(), config.getPassword());
                InputStream is = dl.download(lien);
                nbDownload++;
                File destEpisode = new File(ultimateDest, serie.getNom() + "-" + kv.getKey() + "-" + nbDownload + "." + serie.getExtention());
                try {
                    destEpisode.createNewFile();
                    download(is, destEpisode);
                } catch (IOException ex) {
                    Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "nb d'épisodes DL: " + nbDownload + " sur " + kv.getValue().size());
        }

    }

    public Config createConfig() {
        Gson gson = new Gson();
        File configF = new File("config.json");
        FileReader fr = null;
        try {
            fr = new FileReader(configF);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        Config config = gson.fromJson(fr, Config.class);
        try {
            fr.close();
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return config;
    }

    public void go() {

        Config config = createConfig();

        int nbDownloadFilm = 0;
        if (config.getFilms() != null && !config.getFilms().isEmpty()) {
            for (Film film : config.getFilms()) {
                Downloader dl = DownloaderFactory.getAutoDownloader(film.getLien());
                dl.init(config.getLogin(), config.getPassword());
                downloadFilm(film, config.getDestination(), dl);
                nbDownloadFilm++;
            }
            Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "nb de film DL: " + nbDownloadFilm + " sur " + config.getFilms().size());
        }

        if (config.getSeries() != null && !config.getSeries().isEmpty()) {
            for (Serie serie : config.getSeries()) {
                downloadSerie(serie, config);
            }
        }
    }

    public static void main(String[] args) {
        new NewMain().go();
    }

}
