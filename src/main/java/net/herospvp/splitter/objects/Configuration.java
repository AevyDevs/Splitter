package net.herospvp.splitter.objects;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.herospvp.splitter.SplitterMain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class Configuration {

    private final SplitterMain instance;
    private final Logger logger;

    private final Path dataDirectory;
    private final String configAddress;

    public Configuration(SplitterMain instance) {
        this.instance = instance;
        this.logger = instance.getLogger();
        this.dataDirectory = instance.getDataDirectory();
        this.configAddress = dataDirectory + "/config.json";
    }

    public void load() throws Exception {

        if (!Files.exists(dataDirectory)) {
            Files.createDirectory(dataDirectory);
        }

        File file = new File(configAddress);

        if (file.createNewFile()) {

            JsonObject mainObject = new JsonObject(), lobbies = new JsonObject();

            lobbies.put("lobby-1", 50);
            lobbies.put("lobby-2", 50);

            mainObject.put("lobbies", lobbies);

            BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(configAddress));
            Jsoner.serialize(mainObject, bufferedWriter);
            bufferedWriter.close();
        }

        Reader reader = Files.newBufferedReader(Paths.get(configAddress));
        JsonObject lobbies = (JsonObject) ((JsonObject) Jsoner.deserialize(reader)).get("lobbies");

        List<LobbyInstance> temp = new ArrayList<>();

        lobbies.forEach((s, o) -> {
            Optional<RegisteredServer> opt = instance.getServer().getServer(s);

            if (!opt.isPresent()) {
                logger.warning("The lobby \"" + s + "\" seems to not be present in the velocity's configuration...");
                logger.warning("Therefore, the plugin will ignore that lobby.");
                return;
            }

            temp.add(new LobbyInstance(s, opt.get(), ((BigDecimal) o).intValue()));
            logger.info("Loaded " + s + " as lobby with " + o + " max players.");
        });

        instance.setContainer(new Container(temp));
    }

}
