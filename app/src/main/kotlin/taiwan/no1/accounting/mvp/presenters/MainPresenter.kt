package taiwan.no1.accounting.mvp.presenters

import dagger.internal.Preconditions
import rx.lang.kotlin.subscriber
import taiwan.no1.accounting.domain.CreateFakeUseCase
import taiwan.no1.accounting.internal.di.annotations.PerActivity
import taiwan.no1.accounting.mvp.contracts.MainContract
import taiwan.no1.accounting.mvp.models.FakeModel
import javax.inject.Inject

/**
 *
 * @author  Jieyi Wu
 * @version 0.0.1
 * @since   12/6/16
 */

@PerActivity
class MainPresenter @Inject constructor(val fakeCase: CreateFakeUseCase): MainContract.Presenter {
    // TODO: implement rx lifecycle.
    private lateinit var view: MainContract.View

    //region Subscribers
    private val fakeSubscriber = subscriber<FakeModel>().onCompleted { }.onError { }.onNext { }
    //endregion

    //region View implementation
    override fun setView(view: MainContract.View) {
        Preconditions.checkNotNull(view)

        this.view = view
    }

    override fun init() {
        fakeCase.execute(CreateFakeUseCase.Requests(FakeModel("Jieyi", 19, "H")), this.fakeSubscriber)
    }

    override fun resume() {
    }

    override fun pause() {
    }

    override fun destroy() {
        /* The use case must unsubscribe here. */
        this.fakeCase.unsubscribe()
    }
    //endregion
}
