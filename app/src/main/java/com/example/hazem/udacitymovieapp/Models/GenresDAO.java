package com.example.hazem.udacitymovieapp.Models;

import java.util.HashMap;

/**
 * Created by Hazem on 6/18/2017.
 */

public class GenresDAO {

    private static HashMap<Integer,String> genreMap=null;
    private static HashMap<String,Integer> genreIndexMap=null;
    private GenresDAO ()
    {

    }
    public static String GetGenres(int index)
    {
        if(genreMap==null)
        {
            genreMap=new HashMap<> ();
            genreMap.put(28,"Action");
            genreMap.put(12,"Adventure");
            genreMap.put(16,"Animation");
            genreMap.put(35,"Comedy");
            genreMap.put(80,"Crime");
            genreMap.put(99,"Documentary");
            genreMap.put(18,"Drama");
            genreMap.put(10751,"Family");
            genreMap.put(14,"Fantasy");
            genreMap.put(36,"History");
            genreMap.put(10402,"Music");
            genreMap.put(27,"Horror");
            genreMap.put(9648,"Mystery");
            genreMap.put(10749,"Romance");
            genreMap.put(878,"Science Fiction");
            genreMap.put(10770,"TV Movie");
            genreMap.put(53,"Thriller");
            genreMap.put(10752,"War");
            genreMap.put(37,"Western");
        }
        return genreMap.get(index);
    }
    public static int getGenreIndex(String genre)
    {
        if(genreIndexMap==null)
        {
            genreIndexMap=new HashMap<> ();
            genreIndexMap.put("Action",28);
            genreIndexMap.put("Adventure",12);
            genreIndexMap.put("Animation",16);
            genreIndexMap.put("Comedy",35);
            genreIndexMap.put("Crime",80);
            genreIndexMap.put("Documentary",99);
            genreIndexMap.put("Drama",18);
            genreIndexMap.put("Family",10751);
            genreIndexMap.put("Fantasy",14);
            genreIndexMap.put("History",36);
            genreIndexMap.put("Music",10402);
            genreIndexMap.put("Horror",27);
            genreIndexMap.put("Mystery",9648);
            genreIndexMap.put("Romance",10749);
            genreIndexMap.put("Science Fiction",878);
            genreIndexMap.put("TV Movie",10770);
            genreIndexMap.put("Thriller",53);
            genreIndexMap.put("War",10752);
            genreIndexMap.put("Western",37);
        }
        if(genreIndexMap.containsKey (genre))
        {
            return genreIndexMap.get (genre);
        }
        return -1;
    }
}
