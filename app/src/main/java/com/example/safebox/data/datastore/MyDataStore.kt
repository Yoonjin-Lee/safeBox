package com.example.safebox.data.datastore

import androidx.datastore.core.DataStore
import com.example.safebox.data.datastore.PreferenceKeys.IMAGE_COUNTER
import kotlinx.coroutines.flow.first
import java.util.prefs.Preferences
import javax.inject.Inject

class MyDataStore @Inject constructor(
    private val dataStore: DataStore<Preferences>
){

}