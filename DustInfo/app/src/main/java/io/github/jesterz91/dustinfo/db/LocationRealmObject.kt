package io.github.jesterz91.dustinfo.db

import io.realm.RealmObject

open class LocationRealmObject(
    var name: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0
) : RealmObject()