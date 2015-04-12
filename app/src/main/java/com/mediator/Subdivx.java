package com.mediator;

import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

/**
 * Created by luispablo on 10/04/15.
 */
public class Subdivx implements SubtitlesSource {

    @Override
    public List<Subtitle> search(String text) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://www.subdivx.com/")
                .setConverter(new SimpleXMLConverter())
                .build();

        RetrofitServiceSubdivx service = restAdapter.create(RetrofitServiceSubdivx.class);

        return service.search(text).channel.items;
    }

    public static String findRealLink(Subtitle subtitle) {
        try {
            Document doc = Jsoup.connect(subtitle.getLink()).get();
            return doc.select("a.link1").first().attr("href");
        } catch (IOException e) {
            Logger.e(e);
        }

        return null;
    }
}