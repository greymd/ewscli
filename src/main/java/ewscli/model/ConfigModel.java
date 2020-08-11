package ewscli.model;

public class ConfigModel {
    private String domain;
    private String username;
    private String password;
    private String keyring;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDomain() {
        return domain;
    }

    public String getKeyring() {
        return keyring;
    }

    public boolean isKeyringEnable() {
        return this.keyring.equals("1");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setKeyring(String keyring) {
        this.keyring = keyring;
    }

    @Override
    public String toString() {
        return "ConfigModel{" +
                "domain='" + domain + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", keyring='" + keyring + '\'' +
                '}';
    }
}
