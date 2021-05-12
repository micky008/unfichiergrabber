package com.msc.test1fichier.entity;

import java.io.File;
import java.net.URL;

/**
 *
 * @author Michael
 */
public class Film {

    private String nom;
    private URL lien;
    private File destFolder;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public URL getLien() {
        return lien;
    }

    public void setLien(URL lien) {
        this.lien = lien;
    }

    public File getDestFolder() {
        return destFolder;
    }

    public void setDestFolder(File destFolder) {
        this.destFolder = destFolder;
    }

    
    
}
