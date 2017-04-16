package taiwan.no1.app.mvp.presenters.fragment

import com.devrapid.kotlinknifer.AppLog
import rx.lang.kotlin.subscriber
import taiwan.no1.app.data.source.CloudDataStore
import taiwan.no1.app.domain.usecase.TvLists
import taiwan.no1.app.mvp.contracts.fragment.TvListContract
import taiwan.no1.app.mvp.models.tv.TvBriefModel
import java.util.*

/**
 * @author  Jieyi
 * @since   1/7/17
 */
class TvListPresenter constructor(val tvCase: TvLists): BasePresenter<TvListContract.View>(), TvListContract.Presenter {
    private var tvBriefModelList: List<TvBriefModel> = emptyList()

    //region Presenter implementation
    override fun init(view: TvListContract.View) {
        super.init(view)
    }

    override fun requestListTvs(category: CloudDataStore.Tvs, page: Int) {
        this.view.showLoading()

        val request = TvLists.Requests(category, page)
        request.fragmentLifecycle = this.view.getLifecycle()
        // If declaring [subscriber] as a variable, it won't be used again.
        this.tvCase.execute(request, subscriber<List<TvBriefModel>>().onError {
            AppLog.e(it.message)
            AppLog.e(it)
        }.onNext {
            this.tvBriefModelList += it
            view.showTvBriefList(this.tvBriefModelList)
            this.view.hideLoading()
        }.onCompleted { this.view.hideLoading() })
    }

    override fun restoreTvList(tvList: List<TvBriefModel>) {
        this.tvBriefModelList = tvList.toList()
    }

    override fun getTvList(): ArrayList<TvBriefModel> = ArrayList(this.tvBriefModelList)
    //endregion
}
