package taiwan.no1.app.mvp.views

import com.trello.rxlifecycle.android.FragmentEvent
import rx.Observable

/**
 * This specifies [IFragmentView].
 *
 * @author  Jieyi
 * @since   12/9/16
 */

interface IFragmentView {
    /**
     * Get a fragment life cycle.
     */
    fun getLifecycle(): Observable<FragmentEvent>?
}
