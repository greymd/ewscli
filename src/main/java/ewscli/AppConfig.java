package ewscli;

import ewscli.logic.ConfigIOLogic;
import ewscli.exception.InvalidConfigException;
import ewscli.logic.AuthLogic;
import ewscli.model.ConfigModel;
import microsoft.exchange.webservices.data.core.ExchangeService;

import java.net.URISyntaxException;


public class AppConfig {
    private static AppConfig appConfig = null;

    private static ExchangeService service;
    private static String username;
    private static String domain;

    public static ExchangeService getService() {
        return service;
    }

    private AppConfig () throws InvalidConfigException {
        var cio = new ConfigIOLogic();
        if(!cio.hasConfig()) {
            throw new InvalidConfigException("[Error] Run '" + DefaultParams.APPNAME + " configure' to initialize credentials.");
        }
        ConfigModel config = cio.getConfig();
        try {
            service = AuthLogic.initializeService(config.getUsername(), config.getPassword(), config.getDomain());
            config = null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static synchronized AppConfig getInstance () throws InvalidConfigException {
        if (appConfig == null) {
            appConfig = new AppConfig();
        }
        return appConfig;
    }
}
