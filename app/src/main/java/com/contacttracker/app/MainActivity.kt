package com.contacttracker.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.contacttracker.app.data.AppDatabase
import com.contacttracker.app.data.ContactRepository
import com.contacttracker.app.ui.ContactViewModel
import com.contacttracker.app.ui.ContactViewModelFactory
import com.contacttracker.app.ui.navigation.AppNavigation
import com.contacttracker.app.ui.theme.ContactTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactTrackerApp()
        }
    }
}

@Composable
fun ContactTrackerApp() {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val repository = ContactRepository(database.contactDao())
    val factory = ContactViewModelFactory(repository)
    val viewModel: ContactViewModel = viewModel(factory = factory)
    val navController = rememberNavController()

    ContactTrackerTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation(navController = navController, viewModel = viewModel)
        }
    }
}
