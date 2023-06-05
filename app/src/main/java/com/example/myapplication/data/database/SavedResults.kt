package com.example.myapplication.data.database

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
@Entity(tableName = "results_table")
data class SavedResult(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val rating: Int = 0,
    val maxRating: Int = 0,
    val radius: Int = 0,
    val address: String = "", // place address
    val string: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeInt(rating)
        parcel.writeInt(radius)
        parcel.writeString(address)
        parcel.writeString(string)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedResult> {
        override fun createFromParcel(parcel: Parcel): SavedResult {
            return SavedResult(parcel)
        }

        override fun newArray(size: Int): Array<SavedResult?> {
            return arrayOfNulls(size)
        }
    }
}
