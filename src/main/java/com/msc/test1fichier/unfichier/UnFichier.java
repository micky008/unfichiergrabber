package com.msc.test1fichier.unfichier;

import com.msc.test1fichier.unfichier.HttpClient.Header;
import com.msc.test1fichier.unfichier.HttpClient.HttpResponse;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Michael
 */
public class UnFichier {

    private List<Header> headers;

    public UnFichier() {
    }

    /**
     * premiere methode a faire. initialise les headers.
     *
     * @param login l'email en générale
     * @param password en clair.
     * @return toujours true pour le moment.
     */
    public boolean init(String login, String password) {
        URL loginURL = null;
        try {
            loginURL = new URL("https://1fichier.com/login.pl");
        } catch (MalformedURLException ex) {
            //no error possible
        }

        String formString = "mail=" + login + "&pass=" + password;

        HttpResponse resp = HttpClient.post(loginURL, formString, HttpClient.Content.FORM);
        List<Header> lh = new ArrayList<>();
        for (Map.Entry<String, List<String>> kv : resp.headers.entrySet()) {
            //System.out.println(kv.getKey() + " = " + Arrays.toString(kv.getValue().toArray()));
            if (kv.getKey() == null) {
                continue;
            }
            if (kv.getKey().equals("Set-Cookie")) {
                Header h = new Header();
                h.key = "Cookie";
                h.value = kv.getValue().get(0);
                lh.add(h);
            } else if (kv.getKey().equals("Expires")) {
                Header h = new Header();
                h.key = kv.getKey();
                h.value = kv.getValue().get(0);
                lh.add(h);
            } else if (kv.getKey().equals("Vary")) {
                Header h = new Header();
                h.key = kv.getKey();
                h.value = kv.getValue().get(0);
                lh.add(h);
            }
        }
        Header h = new Header();
        h.key = "show-cm";
        h.value = "no";
        lh.add(h);

        this.headers = lh;
        return true;
    }

    public InputStream download(URL url) {
        return HttpClient.download(url, this.headers);
    }

}
