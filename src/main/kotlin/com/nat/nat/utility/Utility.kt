package com.nat.nat.utility

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

fun setFields(obj: Any, fieldsToChange: List<Pair<String, Any?>>) {
    val nameToProperty = obj::class.memberProperties.associateBy(KProperty<*>::name)
    fieldsToChange.forEach { (propertyName, propertyValue) ->
        nameToProperty[propertyName]
            .takeIf { it is KMutableProperty<*> }
            ?.let { it as KMutableProperty<*> }
            ?.let { it.setter.call(obj, propertyValue) }
    }
}