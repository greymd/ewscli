package ewscli.logic;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;

import java.net.URI;
import java.net.URISyntaxException;

public class AuthLogic {

    public static ExchangeService initializeService(String username, String password, String domain) throws URISyntaxException {
        ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
        ExchangeCredentials credentials = new WebCredentials(username, password);
        service.setCredentials(credentials);
        service.setUrl(new URI(domain));
        return service;
    }
}
