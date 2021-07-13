package ewscli;

import ewscli.subcommands.ConfigureCommand;
import ewscli.subcommands.GeneralCommand;
import ewscli.subcommands.MailCommand;
import picocli.CommandLine;
import picocli.CommandLine.Spec;

@CommandLine.Command(name = DefaultParams.APPNAME,
        mixinStandardHelpOptions = true,
        version = "1.0.0",
        subcommands = {
                GeneralCommand.class,
                MailCommand.class,
                ConfigureCommand.class
        })
public class Main implements Runnable {

    @Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }

    public static String ping() {
        return "ping";
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new Main()).execute(args));
    }
}
