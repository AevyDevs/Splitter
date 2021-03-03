package net.herospvp.splitter.objects;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class LobbyInstance {

    private final String name;
    private final RegisteredServer registeredServer;
    private final int maxPlayers;

    public Collection<Player> getPlayersConnected() {
        return registeredServer.getPlayersConnected();
    }

    public int getPlayersConnectedSize() {
        return getPlayersConnected().size();
    }

}
