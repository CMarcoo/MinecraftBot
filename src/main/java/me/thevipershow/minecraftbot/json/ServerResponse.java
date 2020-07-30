package me.thevipershow.minecraftbot.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.UUID;

public final class ServerResponse {

    public final static JsonDeserializer<ServerResponse> responseDeserializer = (jsonElement, type, jsonDeserializationContext) -> {
        final JsonObject jsonObject = jsonElement.getAsJsonObject();
        final String versionStr = jsonObject.get("version.name").getAsString();
        final int versionProtocol = jsonObject.get("version.protocol").getAsInt();

        final int playersMax = jsonObject.get("players.max").getAsInt();
        final int online = jsonObject.get("players.online").getAsInt();
        final JsonArray sample = jsonObject.get("players.sample").getAsJsonArray();
        final Players.Sample[] samples = new Players.Sample[sample.size()];
        int count = 0;
        for (final JsonElement element : sample) {
            final JsonObject o = element.getAsJsonObject();
            final String sampleName = o.get("name").getAsString();
            final UUID uuid = UUID.fromString(o.get("id").getAsString());
            samples[count] = new Players.Sample(sampleName, uuid);
            count++;
        }

        final String descriptionText = jsonObject.get("description.text").getAsString();
        final String faviconData = jsonObject.get("favicon").getAsString();

        final Version v = new Version(versionStr, versionProtocol);
        final Players p = new Players(playersMax, online, samples);
        final Description d = new Description(descriptionText);
        final Favicon f = new Favicon(faviconData);

        return new ServerResponse(v, p, d, f);
    };

    private final Version version;
    private final Players players;
    private final Description description;
    private final Favicon favicon;

    public ServerResponse(final Version version, final Players players, final Description description, final Favicon favicon) {
        this.version = version;
        this.players = players;
        this.description = description;
        this.favicon = favicon;
    }

    public Version getVersion() {
        return version;
    }

    public Players getPlayers() {
        return players;
    }

    public Description getDescription() {
        return description;
    }

    public Favicon getFavicon() {
        return favicon;
    }

    public final static class Version {
        private final String name;
        private final int protocol;

        public Version(final String name, final int protocol) {
            this.name = name;
            this.protocol = protocol;
        }

        public String getName() {
            return name;
        }

        public int getProtocol() {
            return protocol;
        }
    }

    public final static class Players {
        private final int max;
        private final int online;
        private final Sample[] samples;

        public Players(final int max, final int online, final Sample[] samples) {
            this.max = max;
            this.online = online;
            this.samples = samples;
        }

        public final static class Sample {
            private final String name;
            private final UUID id;

            public String getName() {
                return name;
            }

            public UUID getId() {
                return id;
            }

            public Sample(final String name, final UUID uuid) {
                this.name = name;
                this.id = uuid;
            }
        }
    }

    public final static class Description {
        private final String text;

        public Description(final String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public final static class Favicon {
        private final String data;

        public Favicon(final String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "version=" + version +
                ", players=" + players +
                ", description=" + description +
                ", favicon=" + favicon +
                '}';
    }
}
