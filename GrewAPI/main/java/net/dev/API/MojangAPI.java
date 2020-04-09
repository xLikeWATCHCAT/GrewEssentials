package net.dev.API;

import com.google.gson.*;

import java.io.*;
import java.net.*;

public class MojangAPI {
    private static final String uuidurl = "https://api.minetools.eu/uuid/%name%";
    private static final String uuidurl_mojang = "https://api.mojang.com/users/profiles/minecraft/%name%";
    private static final String uuidurl_backup = "https://api.ashcon.app/mojang/v2/user/%name%";

    private static final String skinurl = "https://api.minetools.eu/profile/%uuid%";
    private static final String skinurl_mojang = "https://sessionserver.mojang.com/session/minecraft/profile/%uuid%?unsigned=false";
    private static final String skinurl_backup = "https://api.ashcon.app/mojang/v2/user/%uuid%";

    // TODO Deal with duplicated code

    /**
     * Returned object needs to be casted to either BungeeCord's property or
     * Mojang's property (old or new)
     *
     * @return Property object (New Mojang, Old Mojang or Bungee)
     **/
    public Object getSkinProperty(String uuid, boolean tryNext) {
        String output;
        try {
            output = readURL(skinurl.replace("%uuid%", uuid));
            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();

            Property property = new Property();

            if (obj.has("raw")) {
                JsonObject raw = obj.getAsJsonObject("raw");

                if (property.valuesFromJson(raw)) {
                    return new com.mojang.authlib.properties.Property("textures", property.getValue(), property.getSignature());
                }
            }
            return null;
        } catch (Exception e) {
            if (tryNext)
                return getSkinPropertyMojang(uuid);
        }
        return null;
    }

    public Object getSkinProperty(String uuid) {
        return getSkinProperty(uuid, true);
    }

    public Object getSkinPropertyMojang(String uuid, boolean tryNext) {
        String output;
        try {
            output = readURL(skinurl_mojang.replace("%uuid%", uuid));
            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();

            Property property = new Property();

            if (property.valuesFromJson(obj)) {
                return new com.mojang.authlib.properties.Property("textures", property.getValue(), property.getSignature());
            }

            return null;
        } catch (Exception e) {
            if (tryNext)
                return getSkinPropertyBackup(uuid);
        }
        return null;
    }

    public Object getSkinPropertyMojang(String uuid) {
        return getSkinPropertyMojang(uuid, true);
    }

    public Object getSkinPropertyBackup(String uuid) {
        String output;
        try {
            output = readURL(skinurl_backup.replace("%uuid%", uuid), 10000);
            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();
            JsonObject textures = obj.get("textures").getAsJsonObject();
            JsonObject rawTextures = textures.get("raw").getAsJsonObject();

            Property property = new Property();
            property.setValue(rawTextures.get("value").getAsString());
            property.setSignature(rawTextures.get("signature").getAsString());

            return new com.mojang.authlib.properties.Property("textures", property.getValue(), property.getSignature());

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param name - Name of the player
     * @return Dash-less UUID (String)
     */
    public String getUUID(String name, boolean tryNext) {
        String output;
        try {
            output = readURL(uuidurl.replace("%name%", name));

            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();

            if (obj.has("status")) {
                if (obj.get("status").getAsString().equalsIgnoreCase("ERR")) {
                    if (tryNext)
                        return getUUIDMojang(name);
                    return null;
                }
            }

            if (obj.get("id").getAsString().equalsIgnoreCase("null"))
                throw new RuntimeException("not premium");

            return obj.get("id").getAsString();
        } catch (IOException e) {
            if (tryNext)
                return getUUIDMojang(name);
        }
        return null;
    }

    public String getUUID(String name) {
        return getUUID(name, true);
    }

    public String getUUIDMojang(String name, boolean tryNext) {
        String output;
        try {
            output = readURL(uuidurl_mojang.replace("%name%", name));

            if (output.isEmpty())
                throw new RuntimeException("not premium");

            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();

            if (obj.has("error")) {
                if (tryNext)
                    return getUUIDBackup(name);
                return null;
            }

            return obj.get("id").getAsString();

        } catch (IOException e) {
            if (tryNext)
                return getUUIDBackup(name);
        }
        return null;
    }

    public String getUUIDMojang(String name) {
        return getUUIDMojang(name, true);
    }

    public String getUUIDBackup(String name) {
        String output;
        try {
            output = readURL(uuidurl_backup.replace("%name%", name), 10000);

            JsonElement element = new JsonParser().parse(output);
            JsonObject obj = element.getAsJsonObject();

            if (obj.has("code")) {
                if (obj.get("code").getAsInt() == 404) {
                    throw new RuntimeException("not premium");
                }
                throw new RuntimeException("alt api failed");
            }

            return obj.get("uuid").getAsString().replace("-", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readURL(String url) throws IOException {
        return readURL(url, 5000);
    }

    private String readURL(String url, int timeout) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "GrewEntials");
        con.setConnectTimeout(timeout);
        con.setReadTimeout(timeout);
        con.setDoOutput(true);

        String line;
        StringBuilder output = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        while ((line = in.readLine()) != null)
            output.append(line);

        in.close();
        return output.toString();
    }

    private class Property {
        private String name;
        private String value;
        private String signature;

        boolean valuesFromJson(JsonObject obj) {
            if (obj.has("properties")) {
                JsonArray properties = obj.getAsJsonArray("properties");
                JsonObject propertiesObject = properties.get(0).getAsJsonObject();

                String signature = propertiesObject.get("signature").getAsString();
                String value = propertiesObject.get("value").getAsString();

                this.setSignature(signature);
                this.setValue(value);

                return true;
            }

            return false;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        String getValue() {
            return value;
        }

        void setValue(String value) {
            this.value = value;
        }

        String getSignature() {
            return signature;
        }

        void setSignature(String signature) {
            this.signature = signature;
        }
    }

    private class HTTPResponse {
        private String output;
        private int status;

        public String getOutput() {
            return output;
        }

        public void setOutput(String output) {
            this.output = output;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
