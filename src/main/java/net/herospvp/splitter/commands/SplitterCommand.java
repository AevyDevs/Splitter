package net.herospvp.splitter.commands;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import net.herospvp.splitter.SplitterMain;
import net.herospvp.splitter.objects.LobbyInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SplitterCommand implements SimpleCommand {

    private final SplitterMain instance;
    private final LobbyInstance[] lobbyInstances;

    public SplitterCommand(SplitterMain instance) {
        this.instance = instance;
        this.lobbyInstances = instance.getContainer().getLobbyInstances();

        CommandManager commandManager = instance.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("splitter").build();
        commandManager.register(commandMeta, this);
    }

    private final TextComponent defaultMessage = Component.text()
            .append(Component.text("Splitter commands:").color(NamedTextColor.DARK_AQUA))
            .append(Component.text("\n- /splitter reload").color(NamedTextColor.DARK_AQUA))
            .append(Component.text(" » ").color(NamedTextColor.GRAY))
            .append(Component.text("Reload the configuration.").color(NamedTextColor.AQUA))
            .append(Component.text("\n- /splitter list").color(NamedTextColor.DARK_AQUA))
            .append(Component.text(" » ").color(NamedTextColor.GRAY))
            .append(Component.text("List the registered lobbies.").color(NamedTextColor.AQUA))
            .build();

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] strings = invocation.arguments();

        if (strings.length != 1) {
            source.sendMessage(defaultMessage);
            return;
        }

        switch (strings[0].toLowerCase()) {
            case "reload": {
                try {
                    instance.getConfiguration().load();
                    source.sendMessage(Component.text(
                            "Configuration reloaded!")
                            .color(NamedTextColor.GREEN)
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    source.sendMessage(Component.text(
                            "Something went wrong, check the ./plugins/splitter/config.json"
                            ).color(NamedTextColor.RED)
                    );
                }
                break;
            }
            case "list": {
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 0; i < lobbyInstances.length; i++) {
                    stringBuilder
                            .append(lobbyInstances[i].getName())
                            .append(" (max: ")
                            .append(lobbyInstances[i].getMaxPlayers())
                            .append(")");

                    if (i != lobbyInstances.length - 1) {
                        stringBuilder.append(", ");
                    }
                }
                source.sendMessage(
                        Component.text("Available lobbies:\n")
                                .color(NamedTextColor.DARK_AQUA)
                                .append(Component.text(stringBuilder.toString())
                                .color(NamedTextColor.AQUA))
                );
                break;
            }
            default: {
                source.sendMessage(defaultMessage);
                break;
            }
        }
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(Invocation invocation) {
        return CompletableFuture.supplyAsync(() -> Arrays.asList("reload", "list"));
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("splitter.command");
    }

}
