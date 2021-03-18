package net.herospvp.splitter.objects;

import com.velocitypowered.api.proxy.server.RegisteredServer;
import lombok.Getter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
public class Container implements Comparator<LobbyInstance> {

    private final LobbyInstance[] lobbyInstances;

    public Container(List<LobbyInstance> list) {
        this.lobbyInstances = new LobbyInstance[list.size()];
        for (int i = 0; i < list.size(); i++) {
            lobbyInstances[i] = list.get(i);
        }
    }

    public LobbyInstance getLobbyFrom(RegisteredServer registeredServer) {
        return Arrays.stream(lobbyInstances)
                .filter(
                        lobby -> lobby.getRegisteredServer() == registeredServer
                ).findFirst().orElse(null);
    }

    @Override
    public int compare(LobbyInstance a, LobbyInstance b) {
        return Integer.compare(a.getPlayersConnectedSize(), b.getPlayersConnectedSize());
    }

}
