package ewscli.util;

import javax.net.ssl.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.net.URL;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Scanner;

public class CertificateImporter {

    public static boolean isTrusted (URL url) {
        try {
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.connect();
            con.disconnect();
        } catch (SSLHandshakeException e) {
            return false;
        } catch (IOException e){
            e.printStackTrace();
        }
        return true;
    }

    public static byte[] getRootX509Certificate(URL url) {
        // Disabling Certificate Validation
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
            // Do nothing
        }
        try {
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.connect();
            Certificate[] certs = con.getServerCertificates();
            con.disconnect();
            return certs[0].getEncoded();
        } catch (IOException | CertificateEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void registerCertificateKeytool (byte[] cert, String alias) {
        String jvm_root = System.getProperties().getProperty("java.home");
        String cacerts_location = jvm_root + File.separator + "lib" + File.separator + "security" + File.separator + "cacerts";
        String keytool_location;

        if (System.getProperty("os.name").startsWith("Win")) {
            keytool_location = jvm_root + File.separator + "bin" + File.separator + "keytool.exe";
        } else {
            keytool_location = jvm_root + File.separator + "bin" + File.separator + "keytool";
        }
        try {
            Path tempFilePath  = Files.createTempFile("ewscli-cert", ".pem");
            File tempFile = tempFilePath.toFile();
            tempFile.deleteOnExit();
            OutputStream os = new FileOutputStream(tempFile);
            os.write(new String("-----BEGIN CERTIFICATE-----\n").getBytes());
            os.write(new String(Base64.getEncoder().encode(cert)).getBytes());
            os.write(new String("\n-----END CERTIFICATE-----\n").getBytes());
            os.close();

            // Delete alias in advance
            ProcessBuilder deleteProcessBuilder = new ProcessBuilder();
            deleteProcessBuilder.command(keytool_location, "-delete", "-alias", alias, "-keystore", cacerts_location, "-storepass", "changeit");
            Process deleteProcess = deleteProcessBuilder.start();
            deleteProcess.waitFor();

            // Register alias
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(keytool_location, "-importcert", "-trustcacerts", "-alias", alias, "-keystore", cacerts_location, "-file", tempFilePath.toAbsolutePath().toString(), "-storepass", "changeit");

            System.err.println("ewscli: execute: " + keytool_location);
            Process process = processBuilder.start();

            OutputStream stdin = process.getOutputStream();
            InputStream stdout = process.getInputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
            writer.write("y\ny\ny\n");
            writer.flush();
            writer.close();

            Scanner scanner = new Scanner(stdout);
            while (scanner.hasNextLine()) {
                System.err.println(scanner.nextLine());
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.err.println("ewscli: Certificate is trusted.");
            } else {
                System.err.println("ewscli: [Error] Failed to trust certificate.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
