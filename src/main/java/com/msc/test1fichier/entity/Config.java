package com.msc.test1fichier.entity;

import java.io.File;
import java.util.List;

/**
 *
 * @author Michael
 */
public class Config {

    private String login;
    private String password;
    private String destFolder;
    private List<Film> films;
    private List<Serie> series;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDestFolder() {
        return destFolder;
    }

    public void setDestFolder(String destFolder) {
        this.destFolder = destFolder;
    }

    public List<Film> getFilms() {
        return films;
    }

    public void setFilms(List<Film> films) {
        this.films = films;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public File getDestination() {
        return new File(this.destFolder);
    }

}
