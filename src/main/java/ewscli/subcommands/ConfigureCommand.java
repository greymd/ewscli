package ewscli.subcommands;

import ewscli.logic.ConfigIOLogic;
import ewscli.exception.InvalidPasswordException;
import picocli.CommandLine;

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
        }
        if (! cpl.hasConfig()) {
            System.err.println("Failed to save config file.");
            System.exit(1);
        }
    }
}
