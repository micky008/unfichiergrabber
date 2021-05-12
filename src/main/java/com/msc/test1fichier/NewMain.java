package com.msc.test1fichier;

import com.google.gson.Gson;
import com.msc.test1fichier.entity.Config;
import com.msc.test1fichier.entity.Film;
import com.msc.test1fichier.entity.Serie;
import com.msc.test1fichier.unfichier.UnFichier;
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

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) throws Exception {
//        byte buf[] = new byte[1024];
//        FileOutputStream fos = new FileOutputStream(new File("test.pdf"));
//        int lu = 0;
//        while ((lu = is.read(buf)) > -1) {
//            fos.write(buf, 0, lu);
//        }
//        fos.flush();
//        fos.close();
//        is.close();
//    }
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

    public void downloadFilm(Config config, UnFichier unFichier) {
        if (config.getFilms() == null) {
            return;
        }

        File destFolder = config.getDestination();

        int nbDownload = 0;

        for (Film film : config.getFilms()) {

            if (film.getDestFolder() != null) {
                destFolder = film.getDestFolder();
                destFolder.mkdirs();
            }

            File destFilm = new File(destFolder, film.getNom());
            try {
                destFilm.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }

            InputStream is = unFichier.download(film.getLien());
            try {
                download(is, destFilm);
                nbDownload++;
            } catch (IOException ex) {
                Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "nb de film DL: " + nbDownload + " sur " + config.getFilms().size());

    }

    public void downloadSerie(Config config, UnFichier unFichier) {
        if (config.getSeries() == null) {
            return;
        }

        File destFolder = config.getDestination();
        int nbDownload = 0;
        for (Serie serie : config.getSeries()) {
            Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "j'attaque la serie: " + serie.getNom());
            File serieDestFolder = new File(destFolder, serie.getNom());
            for (Map.Entry<String, List<URL>> kv : serie.getSaisons().entrySet()) {
                Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "j'attaque la saison: " + kv.getKey() + "(" + kv.getValue().size() + " episodes)");
                File ultimateDest = new File(serieDestFolder, kv.getKey());
                ultimateDest.mkdirs();
                nbDownload = 0;
                    Logger.getLogger(NewMain.class.getName()).log(Level.INFO, "Download en cours");
                for (URL lien : kv.getValue()) {
                    InputStream is = unFichier.download(lien);
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
        UnFichier unficher = new UnFichier();
        unficher.init(config.getLogin(), config.getPassword());
        downloadFilm(config, unficher);
        downloadSerie(config, unficher);
    }

    public static void main(String[] args) {
        new NewMain().go();
    }

}
