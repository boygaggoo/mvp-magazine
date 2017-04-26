package taiwan.no1.app.ui

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewStub
import butterknife.bindView
import com.devrapid.kotlinknifer.AppLog
import com.devrapid.kotlinknifer.resizeView
import com.trello.rxlifecycle.android.FragmentEvent
import com.trello.rxlifecycle.components.support.RxFragment
import dagger.internal.Preconditions
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.subscriber
import taiwan.no1.app.App
import taiwan.no1.app.R
import taiwan.no1.app.internal.di.HasComponent
import taiwan.no1.app.internal.di.components.FragmentComponent
import taiwan.no1.app.mvp.views.IFragmentView
import taiwan.no1.app.mvp.views.IView
import java.util.concurrent.TimeUnit

/**
 * Base presenter for collecting common methods here.
 *
 * @author  Jieyi
 * @since   12/5/16
 */

abstract class BaseFragment: RxFragment(), IView, IFragmentView {
    private val vLoading by bindView<View>(R.id.ll_loading)
    private val vRetry by bindView<View>(R.id.ll_error)
    private val vError by bindView<View>(R.id.ll_error)

    protected var rootView: View? = null
    // TODO: 4/27/17 這邊要修一下!!!
    private var delayPost = Observable.just("").delay(5, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
    private var sub = subscriber<String>().onNext { this@BaseFragment.vLoading.visibility = GONE }.onError {
        AppLog.e(it.message)
        AppLog.e(it)
    }

    //region Fragment lifecycle.
    override final fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                    savedInstanceState: Bundle?): View? {
        // Avoid that an activity is deleted and get null pointer so inject the component here.
        this.inject()
        // Keep the instance data.
        this.retainInstance = true

        // FIXED: https://www.zybuluo.com/kimo/note/255244
        if (null == rootView)
            rootView = inflater.inflate(this.inflateView(), null)
        val parent: ViewGroup? = rootView?.parent as ViewGroup?
        parent?.removeView(rootView)

        // Init the presenter.
        this.initPresenter()

        return rootView
    }

    override final fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.init(savedInstanceState)
    }
    //endregion

    //region Presenter implements
    override fun showLoading() {
        // Before show the loading view resize the view size.
        this.vLoading.resizeView(App.containerWidth, App.containerHeight)
        this.vLoading.visibility = VISIBLE
    }

    override fun hideLoading() {
        // Delay 0.5s then hiding the loading view.
//        Handler().postDelayed({ this@BaseFragment.vLoading.visibility = GONE }, 500)
        val t = this.delayPost.subscribe(this.sub)
        t.unsubscribe()
    }

    override fun showRetry() {
    }

    override fun hideRetry() {
    }

    override fun showError(message: String) {
        Preconditions.checkNotNull(message)
    }

    /**
     * Get the [Context] of this application.
     *
     * @return application [Context].
     */
    /**
     * FIXME: 3/26/17 03-26 07:17:36.275 E/MY_LOG: invoke(MovieDetailPresenter.kt:31):Attempt to invoke virtual method
     * 'android.content.Context android.content.ContextWrapper.getApplicationContext()' on a null object reference
     * 03-26 07:17:36.275 E/MY_LOG: taiwan.no1.app.ui.BaseFragment.context(BaseFragment.kt:78)
     */
    override fun context(): Context = this.activity.applicationContext

    /**
     * Get this fragment lifecycle.
     *
     * @return [Observable] of lifecycle.
     */
    override fun getLifecycle(): Observable<FragmentEvent> = this.lifecycle()
    //endregion

    //region Initialization's order
    /**
     * Initialize the fragment method.
     *
     * @param savedInstanceState before status.
     */
    abstract protected fun init(savedInstanceState: Bundle?)

    /**
     * Injected the presenter and the fragment use case.
     */
    abstract protected fun inject()

    /**
     * Set the view for inflating.
     *
     * @return [LayoutRes] layout xml.
     */
    @LayoutRes
    abstract protected fun inflateView(): Int

    /**
     * Initialize the presenter in [onCreateView].
     */
    abstract protected fun initPresenter()
    //endregion

    /**
     * Get a use case component from a owner activity.
     *
     * @param componentType owner [BaseActivity] class name.
     * @return [FragmentComponent].
     */
    protected fun <C> getComponent(componentType: Class<C>): C =
            componentType.cast((activity as HasComponent<*>).getFragmentComponent())

    /**
     * Show the [viewStub] and prevent to show again and again.
     *
     * @param viewStub [ViewStub] of assignment.
     * @param settings the setting of [ViewStub]'s content.
     */
    protected fun showViewStub(viewStub: ViewStub, settings: () -> Unit) {
        if (null != viewStub.parent) {
            viewStub.inflate()
            settings()
        }
        else {
            viewStub.visibility = View.VISIBLE
            View(this.context())
        }
    }
}
