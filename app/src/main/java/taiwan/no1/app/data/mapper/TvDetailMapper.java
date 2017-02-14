package taiwan.no1.app.data.mapper;

import android.support.annotation.NonNull;

import com.innahema.collections.query.queriables.Queryable;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import taiwan.no1.app.data.entities.TvDetailEntity;
import taiwan.no1.app.domain.mapper.IBeanMapper;
import taiwan.no1.app.mvp.models.CommonModel;
import taiwan.no1.app.mvp.models.FilmCastsModel;
import taiwan.no1.app.mvp.models.FilmImagesModel;
import taiwan.no1.app.mvp.models.FilmVideoModel;
import taiwan.no1.app.mvp.models.MovieListResModel;
import taiwan.no1.app.mvp.models.TvDetailModel;
import taiwan.no1.app.mvp.models.TvSeasonsModel;

/**
 * Mapper class used to transform between {@link TvDetailModel} (in the kotlin layer) and {@link TvDetailEntity}
 * (in the data layer).
 * <p>
 * Created by weian on 2017/1/12.
 */

@Singleton
public class TvDetailMapper implements IBeanMapper<TvDetailModel, TvDetailEntity> {
    @Inject FilmVideosMapper filmVideosMapper;
    @Inject FilmImagesMapper filmImagesMapper;
    @Inject FilmCastsMapper filmCastsMapper;
    @Inject MovieListResMapper movieListResMapper;
    @Inject TvSeasonDetailMapper tvSeasonDetailMapper;

    @Inject
    public TvDetailMapper() {
    }

    /**
     * Implement {@inheritDoc}
     */
    @NonNull
    @Override
    @Deprecated
    public TvDetailEntity transformFrom(@NonNull TvDetailModel model) {
        throw new Error("No-op");
    }

    /**
     * Implement {@inheritDoc}
     */
    @NonNull
    @Override
    public TvDetailModel transformTo(@NonNull TvDetailEntity entity) {
        List<FilmVideoModel> filmVideoModels = Queryable.from(entity.getVideos().getResults())
                                                        .map(filmVideosMapper::transformTo)
                                                        .toList();
        FilmImagesModel filmImagesModel = this.filmImagesMapper.transformTo(entity.getImages());
        MovieListResModel movieListResModel = this.movieListResMapper.transformTo(entity.getSimilar());
        FilmCastsModel filmCastsModel = this.filmCastsMapper.transformTo(entity.getCredits());
        List<TvDetailModel.CreatedByBean> createdByBeans = Queryable.from(entity.getCreated_by())
                                                                    .map(data -> new TvDetailModel.CreatedByBean())
                                                                    .toList();
        List<CommonModel.BaseBean> genresBeen = Queryable.from(entity.getGenres())
                                                         .map(data -> new CommonModel.BaseBean(data.getId(),
                                                                                               data.getName()))
                                                         .toList();
        List<CommonModel.BaseBean> networksBeen = Queryable.from(entity.getNetworks())
                                                           .map(data -> new CommonModel.BaseBean(data.getId(),
                                                                                                 data.getName()))
                                                           .toList();
        List<CommonModel.BaseBean> productionCompaniesBeen = Queryable.from(entity.getProduction_companies())
                                                                      .map(data -> new CommonModel.BaseBean(data.getId(),
                                                                                                            data.getName()))
                                                                      .toList();
        List<TvSeasonsModel> tvSeasonsModels = Queryable.from(entity.getSeasons())
                                                        .map(data -> new TvSeasonsModel("",
                                                                                        data.getAir_date(),
                                                                                        "",
                                                                                        "",
                                                                                        data.getEpisode_count(),
                                                                                        data.getId(),
                                                                                        data.getPoster_path(),
                                                                                        data.getSeason_number(),
                                                                                        null,
                                                                                        null,
                                                                                        null,
                                                                                        null))
                                                        .toList();

        return new TvDetailModel(entity.getBackdrop_path(),
                                 entity.getFirst_air_date(),
                                 entity.getHomepage(),
                                 entity.getId(),
                                 entity.isIn_production(),
                                 entity.getLast_air_date(),
                                 entity.getName(),
                                 entity.getNumber_of_episodes(),
                                 entity.getNumber_of_seasons(),
                                 entity.getOriginal_language(),
                                 entity.getOriginal_name(),
                                 entity.getOverview(),
                                 entity.getPopularity(),
                                 entity.getPoster_path(),
                                 entity.getStatus(),
                                 entity.getType(),
                                 entity.getVote_average(),
                                 entity.getVote_count(), new CommonModel.VideosBean(filmVideoModels),
                                 filmImagesModel,
                                 movieListResModel,
                                 filmCastsModel,
                                 createdByBeans,
                                 entity.getEpisode_run_time(),
                                 genresBeen,
                                 entity.getLanguages(),
                                 networksBeen,
                                 entity.getOrigin_country(),
                                 productionCompaniesBeen, tvSeasonsModels);
    }
}
