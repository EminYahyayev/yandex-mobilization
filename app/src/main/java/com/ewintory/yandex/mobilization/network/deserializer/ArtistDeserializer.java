package com.ewintory.yandex.mobilization.network.deserializer;

import com.ewintory.yandex.mobilization.model.Artist;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class ArtistDeserializer implements JsonDeserializer<Artist> {

    private static String safeGetString(JsonElement element) {
        return (element != null) ? element.getAsString() : null;
    }

    @Override
    public Artist deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final long id = jsonObject.get("id").getAsLong();
        final String name = safeGetString(jsonObject.get("name"));
        final String link = safeGetString(jsonObject.get("link"));
        final String description = safeGetString(jsonObject.get("description"));

        final int tracks = jsonObject.get("tracks").getAsInt();
        final int albums = jsonObject.get("albums").getAsInt();

        final JsonArray genreArray = jsonObject.get("genres").getAsJsonArray();
        final List<String> genres = toStringList(genreArray);

        final JsonObject coverObject = jsonObject.get("cover").getAsJsonObject();
        final String bigCover = safeGetString(coverObject.get("big"));
        final String smallCover = safeGetString(coverObject.get("small"));

        return new Artist()
                .setId(id)
                .setName(name)
                .setGenres(genres)
                .setTracks(tracks)
                .setAlbums(albums)
                .setLink(link)
                .setDescription(description)
                .setSmallCover(smallCover)
                .setBigCover(bigCover);
    }

    private static List<String> toStringList(JsonArray array) {
        List<String> list = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }
        return list;
    }

}
