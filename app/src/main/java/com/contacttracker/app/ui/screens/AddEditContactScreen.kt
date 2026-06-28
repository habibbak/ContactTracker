package com.contacttracker.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.contacttracker.app.data.Contact
import com.contacttracker.app.data.ContactStatus
import com.contacttracker.app.ui.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    viewModel: ContactViewModel,
    contactId: Long?,
    onDone: () -> Unit,
    onBack: () -> Unit
) {
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("F") }
    var phone by remember { mutableStateOf("") }
    var phoneSecondary by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var hostName by remember { mutableStateOf("") }
    var hostPhone by remember { mutableStateOf("") }
    var hostPhoneSecondary by remember { mutableStateOf("") }
    var wantsPrayer by remember { mutableStateOf(false) }
    var wantsOtherSituation by remember { mutableStateOf(false) }
    var contactAtHome by remember { mutableStateOf(false) }
    var contactAtOffice by remember { mutableStateOf(false) }
    var contactAtChurch by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf(ContactStatus.NEW) }
    var notes by remember { mutableStateOf("") }
    var statusMenuExpanded by remember { mutableStateOf(false) }
    var existingContact by remember { mutableStateOf<Contact?>(null) }

    LaunchedEffect(contactId) {
        if (contactId != null) {
            viewModel.getContactById(contactId)?.let { contact ->
                existingContact = contact
                lastName = contact.lastName
                firstName = contact.firstName
                gender = contact.gender.ifBlank { "F" }
                phone = contact.phone
                phoneSecondary = contact.phoneSecondary
                email = contact.email
                hostName = contact.hostName
                hostPhone = contact.hostPhone
                hostPhoneSecondary = contact.hostPhoneSecondary
                wantsPrayer = contact.wantsPrayer
                wantsOtherSituation = contact.wantsOtherSituation
                contactAtHome = contact.contactAtHome
                contactAtOffice = contact.contactAtOffice
                contactAtChurch = contact.contactAtChurch
                status = contact.status
                notes = contact.notes
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contactId == null) "Add contact" else "Edit contact") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
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
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First name") }, modifier = Modifier.fillMaxWidth())

            Text("Gender")
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = gender == "F", onClick = { gender = "F" })
                Text("Female")
                RadioButton(selected = gender == "M", onClick = { gender = "M" })
                Text("Male")
            }

            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Phone number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phoneSecondary, onValueChange = { phoneSecondary = it }, label = { Text("Secondary phone number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email address") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = hostName, onValueChange = { hostName = it }, label = { Text("Host name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = hostPhone, onValueChange = { hostPhone = it }, label = { Text("Host phone number") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = hostPhoneSecondary, onValueChange = { hostPhoneSecondary = it }, label = { Text("Host secondary phone number") }, modifier = Modifier.fillMaxWidth())

            Text("Would like to be contacted for")
            CheckboxRow(label = "Prayer for personal needs", checked = wantsPrayer, onCheckedChange = { wantsPrayer = it })
            CheckboxRow(label = "Other particular situation", checked = wantsOtherSituation, onCheckedChange = { wantsOtherSituation = it })

            Text("Would like to be contacted")
            CheckboxRow(label = "At home", checked = contactAtHome, onCheckedChange = { contactAtHome = it })
            CheckboxRow(label = "At the office", checked = contactAtOffice, onCheckedChange = { contactAtOffice = it })
            CheckboxRow(label = "At one of our churches", checked = contactAtChurch, onCheckedChange = { contactAtChurch = it })

            ExposedDropdownMenuBox(expanded = statusMenuExpanded, onExpandedChange = { statusMenuExpanded = it }) {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusMenuExpanded) },
                    modifier = Modifier.fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = statusMenuExpanded, onDismissRequest = { statusMenuExpanded = false }) {
                    ContactStatus.ALL.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                status = option
                                statusMenuExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )

            Button(
                onClick = {
                    val contact = Contact(
                        id = existingContact?.id ?: 0,
                        lastName = lastName,
                        firstName = firstName,
                        gender = gender,
                        phone = phone,
                        phoneSecondary = phoneSecondary,
                        email = email,
                        hostName = hostName,
                        hostPhone = hostPhone,
                        hostPhoneSecondary = hostPhoneSecondary,
                        wantsPrayer = wantsPrayer,
                        wantsOtherSituation = wantsOtherSituation,
                        contactAtHome = contactAtHome,
                        contactAtOffice = contactAtOffice,
                        contactAtChurch = contactAtChurch,
                        status = status,
                        notes = notes,
                        createdAt = existingContact?.createdAt ?: System.currentTimeMillis()
                    )
                    if (existingContact == null) {
                        viewModel.addContact(contact)
                    } else {
                        viewModel.updateContact(contact)
                    }
                    onDone()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
private fun CheckboxRow(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label)
    }
}
