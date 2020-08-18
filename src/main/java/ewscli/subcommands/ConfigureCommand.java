package ewscli.subcommands;

import ewscli.logic.ConfigIOLogic;
import ewscli.exception.InvalidPasswordException;
import picocli.CommandLine;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

@CommandLine.Command(name = "configure",
        description = "Initialize credentials",
        mixinStandardHelpOptions = true)
public class ConfigureCommand implements Runnable {

    @Override
    public void run() {
        var cpl = new ConfigIOLogic();
        try {
            cpl.initializeConfig();
        } catch (InvalidPasswordException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        } catch (MalformedURLException | URISyntaxException e) {
            System.err.println("ewscli: [Error] Provided URL is not valid.");
            System.exit(1);
        }
        if (! cpl.hasConfig()) {
            System.err.println("ewscli: [Error] Failed to save config file.");
            System.exit(1);
        }
    }
}
