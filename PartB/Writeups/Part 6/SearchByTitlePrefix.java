/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package student;

import java.io.FileNotFoundException;
import java.util.stream.Stream;

/**
 *
 * @author eliot
 */
public class SearchByTitlePrefix {

    Song.CmpTitle cmp = new Song.CmpTitle();

    // keep a local direct reference to the song array
    private RaggedArrayList<Song> songs;

    public SearchByTitlePrefix(SongCollection sc) {
        songs = new RaggedArrayList<Song>(cmp);
        int songCount = 0;
        for (Song song : sc.getAllSongs()) {
            songs.add(song);
        }
        songs.stats();
    }
    
    public static String nextWord(String str) {

        // if string is empty
        if (str.equals("")) {
            return "a";
        }

        // Find first character from
        // right which is not z.
        int i = str.length() - 1;
        while (str.charAt(i) == 'z' && i >= 0) {
            i--;
        }

        // If all characters are 'z', 
        // append an 'a' at the end.
        if (i == -1) {
            str = str + 'a';
        } // If there are some
        // non-z characters 
        else {
            str = str.substring(0, i)
                    + (char) ((int) (str.charAt(i)) + 1)
                    + str.substring(i + 1);
        }
        return str;
    }
    public Song[] search(String prefix) {

        String nextTitle = nextWord(prefix);
        
        RaggedArrayList<Song> sublist = songs.subList(new Song("", prefix, ""), new Song("", nextTitle, ""));
        sublist.stats();
        Song[] results = new Song[sublist.size()];
        sublist.toArray(results);
        
        return results;

    }

    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0) {
            System.err.println("usage: prog songfile [search string]");
            return;
        }
        
        SongCollection sc = new SongCollection(args[0]);
        SearchByTitlePrefix sbtp = new SearchByTitlePrefix(sc);
        if (args.length > 1) {
            
            Song[] byArtistResult = sbtp.search(args[1]);
            System.out.printf("searching for: %s\n\n", args[1]);
            Stream.of(byArtistResult).limit(10).forEach(System.out::println);
            System.out.println();
            System.out.printf("Total number of results: %d\n", byArtistResult.length);
        }
    }
}

