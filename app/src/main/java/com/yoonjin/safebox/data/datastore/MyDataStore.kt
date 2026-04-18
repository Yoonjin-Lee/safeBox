package com.yoonjin.safebox.data.datastore

import androidx.datastore.core.DataStore
import java.util.prefs.Preferences
import javax.inject.Inject

class MyDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

}