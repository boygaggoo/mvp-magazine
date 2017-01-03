package taiwan.no1.app.data.mapper;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import taiwan.no1.app.data.entities.MovieBriefEntity;
import taiwan.no1.app.data.entities.MovieListWithDateResEntity;
import taiwan.no1.app.domain.mapper.IBeanMapper;
import taiwan.no1.app.mvp.models.MovieBriefModel;
import taiwan.no1.app.mvp.models.MovieDatesModel;
import taiwan.no1.app.mvp.models.MovieListWithDateResModel;

/**
 * @author Jieyi
 * @version 0.0.1
 * @since 1/1/17
 */

@Singleton
public class MovieListWithDatesResMapper implements IBeanMapper<MovieListWithDateResModel, MovieListWithDateResEntity> {

    @Inject MovieBriefMapper mapper;

    @Inject
    public MovieListWithDatesResMapper() {
    }

    @NonNull
    @Override
    @Deprecated
    public MovieListWithDateResEntity transformFrom(@NonNull MovieListWithDateResModel model) {
        throw new Error("No-op");
    }

    @NonNull
    @Override
    public MovieListWithDateResModel transformTo(@NonNull MovieListWithDateResEntity entity) {
        List<MovieBriefModel> movieBriefModel = new ArrayList<>();
        for (MovieBriefEntity movieBriefEntity : entity.getMovieEntities()) {
            movieBriefModel.add(this.mapper.transformTo(movieBriefEntity));
        }

        return new MovieListWithDateResModel(entity.getPage(),
                                             entity.getTotal_results(),
                                             entity.getTotal_pages(),
                                             null != entity.getDates() ?
                                                     new MovieDatesModel(entity.getDates().getMaximum(),
                                                                         entity.getDates().getMinimum()) :
                                                     null,
                                             movieBriefModel);
    }
}