package com.contacttracker.app.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.contacttracker.app.data.CsvImporter
import com.contacttracker.app.ui.ContactViewModel
import com.contacttracker.app.ui.components.ContactCard
import java.io.BufferedReader
import java.io.InputStreamReader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactViewModel,
    onContactClick: (Long) -> Unit,
    onAddClick: () -> Unit
) {
    val contacts by viewModel.filteredContacts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val importMessage by viewModel.importMessage.collectAsState()
    val context = LocalContext.current

    val csvPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val text = inputStream?.use { stream ->
                BufferedReader(InputStreamReader(stream, Charsets.UTF_8)).readText()
            } ?: ""
            val importedContacts = CsvImporter.parse(text)
            viewModel.importContacts(importedContacts)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contacts") },
                actions = {
                    IconButton(onClick = { csvPickerLauncher.launch(arrayOf("text/*")) }) {
                        Icon(Icons.Filled.UploadFile, contentDescription = "Import CSV")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Filled.Add, contentDescription = "Add contact")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::setSearchQuery,
                label = { Text("Search") },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            importMessage?.let { message ->
                Snackbar(
                    modifier = Modifier.padding(8.dp),
                    action = {
                        IconButton(onClick = { viewModel.clearImportMessage() }) {
                            Text("OK")
                        }
                    }
                ) {
                    Text(message)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts) { contact ->
                    ContactCard(contact = contact, onClick = { onContactClick(contact.id) })
                }
            }
        }
    }
}
