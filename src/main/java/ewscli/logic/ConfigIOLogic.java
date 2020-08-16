package ewscli.logic;

import ewscli.DefaultParams;
import ewscli.exception.InvalidConfigException;
import ewscli.exception.InvalidPasswordException;
import ewscli.model.ConfigModel;
import ewscli.util.ConfigFile;
import ewscli.util.KeyRing;

import java.io.Console;
import java.util.Arrays;

public class ConfigIOLogic {
    public void initializeConfig() throws InvalidPasswordException {
        Console console = System.console();
        if (console == null) {
            System.err.println("Console is not available.");
            System.exit(1);
        }
        var domain = showInputPrompt("EWS endpoint (i.e https://example.com/EWS/exchange.asmx)", console, false);
        var username = showInputPrompt("Username", console, false);
        var password = showInputPrompt("Password", console, true);
        var config = new ConfigModel();
        config.setDomain(domain);
        config.setUsername(username);
        config.setPassword(password);
        saveConfig(config);
        // TODO: Try to access EWS endpoint with simple method
        // If it does not work, ask user import SSL certificate into local JVM to trust the endpoint or not
        // Disabling validation and create pem file
        // Import pem into cacerts
    }

    public String getKeyringServiceName (ConfigModel config) {
        return String.join("_", new String[]{DefaultParams.APPNAME, config.getDomain()});
    }

    private void saveConfig(ConfigModel config) {
        var cfg = new ConfigFile();
        var path = DefaultParams.defaultConfigPath();
        cfg.create(path);
        var keyring = new KeyRing();
        if (keyring.isKeyStoreAvailable()) {
            var serviceName = getKeyringServiceName(config);
            keyring.store(serviceName, config.getUsername(), config.getPassword());
            cfg.setProperty("keyring", "1");
            cfg.setProperty("domain", config.getDomain());
            cfg.setProperty("username", config.getUsername());
            System.out.println("Credential is stored in " + KeyRing.getKeychainName());
        } else {
            cfg.setProperty("keyring", "0");
            cfg.setProperty("domain", config.getDomain());
            cfg.setProperty("username", config.getUsername());
            cfg.setProperty("password", config.getPassword());
        }
        cfg.save(path);
        System.out.println("Configure is stored in " + path);
    }

    public ConfigModel getConfig() throws InvalidConfigException {
        ConfigFile configFile = new ConfigFile();
        configFile.load(DefaultParams.defaultConfigPath());
        var config = new ConfigModel();
        config.setDomain(configFile.getProperty("domain"));
        config.setUsername(configFile.getProperty("username"));
        config.setKeyring(configFile.getProperty("keyring"));;
        if(config.isKeyringEnable()){
            var keyring = new KeyRing();
            if(!keyring.isKeyStoreAvailable()) {
                throw new InvalidConfigException(KeyRing.getKeychainName() + " is not available");
            }
            var serviceName = getKeyringServiceName(config);
            if(!keyring.hasKey(serviceName, config.getUsername())){
                throw new InvalidConfigException(KeyRing.getKeychainName() + " does not have the credential. Rerun '" + DefaultParams.APPNAME + " configure'.");
            }
            var password = keyring.getValue(serviceName, config.getUsername());
            config.setPassword(password);
        } else {
            var password = configFile.getProperty("password");
            config.setPassword(password);
        }
        return config;
    }

    private String showInputPrompt(String prompt, Console console, boolean secure) throws InvalidPasswordException {
        if (secure) {
            System.out.print(prompt + ": ");
            var pass = console.readPassword();
            System.out.print(prompt + " Again: ");
            var passAgain = console.readPassword();
            if (Arrays.equals(pass, passAgain)) {
                return String.valueOf(pass);
            } else {
                throw new InvalidPasswordException("[Error] Passwords do not match each other");
            }
        } else {
            System.out.print(prompt + ": ");
            String input = console.readLine();
            return input;
        }
    }

    public boolean hasConfig() {
        var cfg = new ConfigFile();
        cfg.load(DefaultParams.defaultConfigPath());
        return cfg.hasNecessaryConfigures("keyring", "domain", "username");
    }
}
