package com.msc.test1fichier.entity;

import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Michael
 */
public class Serie {

    private String nom;
    private Map<String, List<URL>> saisons; //s1 : [http://xxx; http://www; etc...]
    private String extention;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Map<String, List<URL>> getSaisons() {
        return saisons;
    }

    public void setSaisons(Map<String, List<URL>> saisons) {
        this.saisons = saisons;
    }

    public String getExtention() {
        return extention;
    }

    public void setExtention(String extention) {
        this.extention = extention;
    }

}
