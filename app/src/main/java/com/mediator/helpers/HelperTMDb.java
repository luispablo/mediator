package com.mediator.helpers;

import com.mediator.model.VideoEntry;
import com.mediator.model.tmdb.TMDbMovieSearchResult;

/**
 * Created by luispablo on 20/05/15.
 */
public class HelperTMDb {

    private VideoEntry videoEntry;

    public HelperTMDb(VideoEntry videoEntry) {
        this.videoEntry = videoEntry;
    }

    public VideoEntry apply(TMDbMovieSearchResult tmdbMovieSearchResult) {
        videoEntry.setVideoType(VideoEntry.VideoType.MOVIE);
        videoEntry.setTitle(tmdbMovieSearchResult.getTitle());
        videoEntry.setEpisodeNumber(-1);
        videoEntry.setSeriesTitle(null);
        videoEntry.setPosterPath(tmdbMovieSearchResult.getPosterPath());
        videoEntry.setSeasonNumber(-1);
        videoEntry.setTmdbId(tmdbMovieSearchResult.getId());

        return videoEntry;
    }
}