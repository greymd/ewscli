package ewscli.util;

import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class CertificateImporterTest {

    @Test
    public void domainFromURL() {
        var importer = new CertificateImporter();
        try {
            assertEquals("exchange.example.com", importer.domainFromURL("https://exchange.example.com/EWS/exchange.asmx"));
            assertEquals("exchange.example.com", importer.domainFromURL("http://exchange.example.com/EWS/exchange.asmx"));
            // assertEquals("exchange.example.com", importer.domainFromURL("exchange.example.com/EWS/exchange.asmx"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void importCertificate() {
        var importer = new CertificateImporter();
        importer.importCertificate("https://example.com/");
    }
}