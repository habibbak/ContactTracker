package com.contacttracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.contacttracker.app.data.Contact
import com.contacttracker.app.ui.ContactViewModel
import com.contacttracker.app.ui.components.StatusBadge
import com.contacttracker.app.util.IntentUtils
import com.contacttracker.app.util.MessageTemplates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    viewModel: ContactViewModel,
    contactId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDeleted: () -> Unit
) {
    val context = LocalContext.current
    var contact by remember { mutableStateOf<Contact?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(contactId) {
        contact = viewModel.getContactById(contactId)
    }

    val current = contact ?: return

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete contact") },
            text = { Text("Are you sure you want to delete ${current.fullName}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteContact(current)
                    showDeleteDialog = false
                    onDeleted()
                }) { Text("Delete") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(current.fullName) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(current.id) }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatusBadge(status = current.status)

            HorizontalDivider()

            Text("Contact actions", style = MaterialTheme.typography.titleMedium)
            val greeting = MessageTemplates.buildGreetingMessage(current)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { IntentUtils.openWhatsApp(context, current.phone, greeting) }) {
                    Text("WhatsApp")
                }
                OutlinedButton(onClick = { IntentUtils.openSms(context, current.phone, greeting) }) {
                    Text("SMS")
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { IntentUtils.dialPhone(context, current.phone) }) {
                    Icon(Icons.Filled.Call, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                    Text("Call")
                }
                if (current.email.isNotBlank()) {
                    OutlinedButton(onClick = {
                        IntentUtils.sendEmail(context, current.email, "Follow-up", greeting)
                    }) {
                        Icon(Icons.Filled.Email, contentDescription = null, modifier = Modifier.padding(end = 4.dp))
                        Text("Email")
                    }
                }
            }

            HorizontalDivider()

            Text("Information", style = MaterialTheme.typography.titleMedium)
            InfoRow("Gender", current.gender)
            InfoRow("Phone", current.phone)
            if (current.phoneSecondary.isNotBlank()) InfoRow("Secondary phone", current.phoneSecondary)
            if (current.email.isNotBlank()) InfoRow("Email", current.email)
            if (current.hostName.isNotBlank()) InfoRow("Host", current.hostName)
            if (current.hostPhone.isNotBlank()) InfoRow("Host phone", current.hostPhone)
            if (current.hostPhoneSecondary.isNotBlank()) InfoRow("Host secondary phone", current.hostPhoneSecondary)

            HorizontalDivider()

            Text("Wants to be contacted for", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (current.wantsPrayer) AssistChip(onClick = {}, label = { Text("Prayer for personal needs") })
                if (current.wantsOtherSituation) AssistChip(onClick = {}, label = { Text("Other situation") })
            }

            Text("Wants to be contacted", style = MaterialTheme.typography.titleMedium)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (current.contactAtHome) AssistChip(onClick = {}, label = { Text("At home") })
                if (current.contactAtOffice) AssistChip(onClick = {}, label = { Text("At the office") })
                if (current.contactAtChurch) AssistChip(onClick = {}, label = { Text("At church") })
            }

            if (current.notes.isNotBlank()) {
                HorizontalDivider()
                Text("Notes", style = MaterialTheme.typography.titleMedium)
                Text(current.notes)
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
