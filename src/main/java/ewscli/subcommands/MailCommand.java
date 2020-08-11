package ewscli.subcommands;

import ewscli.subcommands.mail.*;
import picocli.CommandLine;

@CommandLine.Command(name = "mail",
        mixinStandardHelpOptions = true,
        subcommands = {
                DescribeFoldersCommand.class,
                DescribeMailsCommand.class,
                DescribeMailCommand.class,
                DeleteMailCommand.class,
                DeleteMailsCommand.class,
                CreateMailCommand.class,
                MonitorFoldersCommand.class
        })
public class MailCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }
}
