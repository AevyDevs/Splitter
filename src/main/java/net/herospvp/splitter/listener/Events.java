package net.herospvp.splitter.listener;

import com.velocitypowered.api.event.PostOrder;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.herospvp.splitter.SplitterMain;
import net.herospvp.splitter.objects.Container;
import net.herospvp.splitter.objects.LobbyInstance;
import net.herospvp.splitter.utils.Sorting;

import java.util.Arrays;
import java.util.Optional;

public class Events {

    private final Container container;
    private final LobbyInstance[] lobbyInstances;

    public Events(SplitterMain instance) {
        this.container = instance.getContainer();
        this.lobbyInstances = container.getLobbyInstances();
        instance.getServer().getEventManager().register(instance, this);
    }

    @Subscribe(order = PostOrder.LAST)
    public void on(ServerPreConnectEvent event) {

        Optional<RegisteredServer> serverOptional = event.getResult().getServer();
        if (!serverOptional.isPresent()) {
            return;
        }
        RegisteredServer registeredServer = serverOptional.get();

        LobbyInstance lobbyInstance = container.getLobbyFrom(registeredServer);
        if (lobbyInstance == null) {
            return;
        }

        Sorting.quickSort(lobbyInstances, container, 0, lobbyInstances.length - 1);
        lobbyInstance = container.getLobbyInstances()[0];

        RegisteredServer lobby = lobbyInstance.getRegisteredServer();
        if (lobby == registeredServer) {
            return;
        }

        // if the first one is full
        if (lobbyInstance.getPlayersConnectedSize() >= lobbyInstance.getMaxPlayers()) {

            // checks every other server, some of them may be capable of more players
            LobbyInstance finalLobbyInstance = lobbyInstance;
            Optional<LobbyInstance> optLobby = Arrays.stream(lobbyInstances)
                    .filter(
                            instance -> instance.getPlayersConnectedSize() < finalLobbyInstance.getMaxPlayers()
                    ).findFirst();

            if (!optLobby.isPresent()) {
                // every server is full :(
                return;
            }

            // there is a free server :)
            lobby = optLobby.get().getRegisteredServer();
        }

        // connect to the server
        event.setResult(ServerPreConnectEvent.ServerResult.allowed(lobby));
    }

}
