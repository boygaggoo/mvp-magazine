package taiwan.no1.app.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import butterknife.bindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import taiwan.no1.app.R
import taiwan.no1.app.api.config.MovieDBConfig
import taiwan.no1.app.mvp.models.MovieCastsModel

/**
 *
 * @author  Jieyi
 * @version 0.0.1
 * @since   12/30/16
 */

class MovieCastsAdapter(val context: Context, val casts: List<MovieCastsModel.CastBean>):
        RecyclerView.Adapter<MovieCastsAdapter.MovieCastsViewHolder>() {
    override fun onBindViewHolder(holder: MovieCastsViewHolder, position: Int) {
        Glide.with(this.context.applicationContext).
                load(MovieDBConfig.BASAE_IMAGE_URL + this.casts[position].profile_path).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                into(holder.ivCast)
    }

    override fun getItemCount(): Int = this.casts.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieCastsViewHolder {
        return MovieCastsViewHolder(LayoutInflater.from(this.context).
                inflate(R.layout.item_movie_casts_crews, parent, false))
    }

    class MovieCastsViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val item by bindView<RelativeLayout>(R.id.item_cast)
        val ivCast by bindView<ImageView>(R.id.iv_cast)
    }
}