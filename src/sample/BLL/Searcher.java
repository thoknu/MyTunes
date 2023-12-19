package sample.BLL;

import sample.BE.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality for searching within a list of songs.
 */
public class Searcher {

    /**
     * Searches for songs based on a query.
     * @param searchBase the list of songs to search through.
     * @param query the search query.
     * @return a list of songs that match the query.
     */
    public List<Song> search(List<Song> searchBase, String query) {
        List<Song> searchResult = new ArrayList<>();

        for (Song song : searchBase) {
            if (matchesQuery(song, query)) {
                searchResult.add(song);
            }
        }
        return searchResult;
    }

    /**
     * Checks if a song matches the search query.
     * @param song the song to check.
     * @param query the search query.
     * @return true if the song matches the query.
     */
    private boolean matchesQuery(Song song, String query) {
        return compareToSongTitle(query, song) ||
                compareToSongArtist(query, song) ||
                compareToSongCategory(query, song);
    }

    private boolean compareToSongCategory(String query, Song song) {
        return song.getCategory().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongArtist(String query, Song song) {
        return song.getArtist().toLowerCase().contains(query.toLowerCase());
    }

    private boolean compareToSongTitle(String query, Song song) {
        return song.getTitle().toLowerCase().contains(query.toLowerCase());
    }
}
