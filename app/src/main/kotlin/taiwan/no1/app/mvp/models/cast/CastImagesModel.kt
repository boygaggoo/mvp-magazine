package taiwan.no1.app.mvp.models.cast

import android.os.Parcel
import android.os.Parcelable
import taiwan.no1.app.mvp.models.ImageProfileModel

/**
 *
 * @author  Jieyi
 * @since   1/1/17
 */

data class CastImagesModel(val profiles: List<ImageProfileModel>? = null): Parcelable {
    //region Parcelable
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<CastImagesModel> = object: Parcelable.Creator<CastImagesModel> {
            override fun createFromParcel(source: Parcel): CastImagesModel = CastImagesModel(source)
            override fun newArray(size: Int): Array<CastImagesModel?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel): this(source.createTypedArrayList(ImageProfileModel.CREATOR))

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeTypedList(profiles)
    }
    //endregion
}
