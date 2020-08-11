package ewscli.subcommands;

import ewscli.subcommands.general.DescribeFoldersCommand;
import ewscli.subcommands.general.DescribeItemsCommand;
import picocli.CommandLine;

@CommandLine.Command(name = "general",
        mixinStandardHelpOptions = true,
        subcommands = {
                DescribeItemsCommand.class,
                DescribeFoldersCommand.class
        })
public class GeneralCommand implements Runnable {
    @CommandLine.Spec
    CommandLine.Model.CommandSpec spec;

    @Override
    public void run() {
        spec.commandLine().usage(System.err);
    }
}
