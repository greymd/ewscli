package ewscli.util;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class CertificateImporter {
    public String domainFromURL (String url) throws URISyntaxException {
        URI uri;
        uri = new URI(url);
        return uri.getHost();
    }

    public void importCertificate (String urlString) {
        // Disabling Certificate Validation
/*
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
        };
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        }
        */

        URL url;
        try {
            url = new URL(urlString);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.connect();
            con.getServerCertificates();
            con.disconnect();
            //dumpl all cert info
            print_https_cert(con);
            //dump all the content
            // print_content(con);
        } catch (SSLHandshakeException e) {
            System.err.println("Due to untrusted certificate: " + e.getMessage());
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void print_https_cert(HttpsURLConnection con){

        if(con!=null){

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("Cert Encoded: "
                    + new String(Base64.getEncoder().encode(cert.getEncoded())));
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            } catch (CertificateEncodingException e) {
                e.printStackTrace();
            }

        }

    }

    private void print_content(HttpsURLConnection con){
        if(con!=null){

            try {

                System.out.println("****** Content of the URL ********");
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    System.out.println(input);
                }
                br.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
