package net.herospvp.splitter;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import lombok.Setter;
import net.herospvp.splitter.commands.SplitterCommand;
import net.herospvp.splitter.listener.Events;
import net.herospvp.splitter.objects.Configuration;
import net.herospvp.splitter.objects.Container;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(
        id = "splitter",
        name = "Splitter",
        version = "1.0.1-SNAPSHOT",
        url = "https://www.herospvp.net",
        description = "Keep balance in your lobbies!",
        authors = { "Sorridi" }
)

@Getter
public class SplitterMain {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private final CommandManager commandManager;

    private Configuration configuration;
    @Setter
    private Container container;

    @Inject
    public SplitterMain(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
        this.commandManager = server.getCommandManager();
    }

    @Subscribe
    public void on(ProxyInitializeEvent event) {
        this.configuration = new Configuration(this);

        try {
            configuration.load();
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe("It wasn't possible to load the configuration, the plugin will not be enabled!");
            return;
        }

        new Events(this);
        new SplitterCommand(this);
    }

}
