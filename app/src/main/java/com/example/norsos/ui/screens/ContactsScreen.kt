package com.example.norsos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.norsos.model.EmergencyContact
import com.example.norsos.services.EmergencyDispatcher
import com.example.norsos.ui.EmergencyViewModel
import com.example.norsos.ui.theme.AlertAmber
import com.example.norsos.ui.theme.SosRed

@Composable
fun ContactsScreen(viewModel: EmergencyViewModel) {
    val context = LocalContext.current
    val contacts by viewModel.contacts.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val lang = settings.language
    val locationData by viewModel.currentLocation.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var editingContact by remember { mutableStateOf<EmergencyContact?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = SosRed,
                contentColor = Color.White,
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .testTag("add_contact_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = com.example.norsos.localization.AppStrings.addNewContact(lang))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = com.example.norsos.localization.AppStrings.contactsTitle(lang),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = com.example.norsos.localization.AppStrings.contactsSubtitle(lang),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (contacts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(com.example.norsos.localization.AppStrings.noContactsYet(lang), fontWeight = FontWeight.SemiBold)
                            Text(com.example.norsos.localization.AppStrings.tapPlusToAdd(lang), fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            } else {
                items(contacts, key = { it.id }) { contact ->
                    ContactItemCard(
                        contact = contact,
                        lang = lang,
                        onCall = { EmergencyDispatcher.dialNumber(context, contact.phoneNumber) },
                        onSms = {
                            val loc = locationData?.googleMapsUrl ?: "Konum bekleniyor"
                            val msg = settings.customMessage.replace("{location}", loc)
                            EmergencyDispatcher.sendSms(context, contact.phoneNumber, msg)
                        },
                        onEdit = { editingContact = contact },
                        onDelete = { viewModel.deleteContact(contact.id) },
                        onSetPrimary = { viewModel.setPrimaryContact(contact.id) }
                    )
                }
            }
        }
    }

    if (showAddDialog) {
        ContactDialog(
            title = com.example.norsos.localization.AppStrings.addNewContact(lang),
            contact = null,
            lang = lang,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, phone, rel, isPrimary ->
                viewModel.addContact(name, phone, rel, isPrimary)
                showAddDialog = false
            }
        )
    }

    editingContact?.let { contactToEdit ->
        ContactDialog(
            title = com.example.norsos.localization.AppStrings.editContact(lang),
            contact = contactToEdit,
            lang = lang,
            onDismiss = { editingContact = null },
            onConfirm = { name, phone, rel, isPrimary ->
                viewModel.updateContact(
                    contactToEdit.copy(
                        name = name,
                        phoneNumber = phone,
                        relationship = rel,
                        isPrimary = isPrimary
                    )
                )
                editingContact = null
            }
        )
    }
}

@Composable
private fun ContactItemCard(
    contact: EmergencyContact,
    lang: com.example.norsos.model.AppLanguage,
    onCall: () -> Unit,
    onSms: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetPrimary: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (contact.isPrimary)
                SosRed.copy(alpha = 0.08f)
            else
                MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("contact_item_${contact.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            if (contact.isPrimary) SosRed else MaterialTheme.colorScheme.secondaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.name.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = if (contact.isPrimary) Color.White else MaterialTheme.colorScheme.onSecondaryContainer,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = contact.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (contact.isPrimary) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Birincil",
                                tint = AlertAmber,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "${contact.relationship} • ${contact.phoneNumber}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onCall, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Call, contentDescription = "Ara", tint = MaterialTheme.colorScheme.primary)
                }

                IconButton(onClick = onSms, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Message, contentDescription = "SMS", tint = MaterialTheme.colorScheme.secondary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!contact.isPrimary) {
                    TextButton(onClick = onSetPrimary) {
                        Text(com.example.norsos.localization.AppStrings.makePrimary(lang), fontSize = 12.sp)
                    }
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Düzenle", modifier = Modifier.size(18.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Sil", tint = SosRed, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun ContactDialog(
    title: String,
    contact: EmergencyContact?,
    lang: com.example.norsos.model.AppLanguage,
    onDismiss: () -> Unit,
    onConfirm: (name: String, phone: String, relationship: String, isPrimary: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(contact?.name ?: "") }
    var phone by remember { mutableStateOf(contact?.phoneNumber ?: "") }
    var relationship by remember { mutableStateOf(contact?.relationship ?: "Yakın") }
    var isPrimary by remember { mutableStateOf(contact?.isPrimary ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(com.example.norsos.localization.AppStrings.nameLabel(lang)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text(com.example.norsos.localization.AppStrings.phoneLabel(lang)) },
                    placeholder = { Text("05xxxxxxxxx") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = relationship,
                    onValueChange = { relationship = it },
                    label = { Text(com.example.norsos.localization.AppStrings.relationshipLabel(lang)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(com.example.norsos.localization.AppStrings.setAsPrimaryLabel(lang), style = MaterialTheme.typography.bodySmall)
                    Switch(checked = isPrimary, onCheckedChange = { isPrimary = it })
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        onConfirm(name, phone, relationship, isPrimary)
                    }
                },
                enabled = name.isNotBlank() && phone.isNotBlank()
            ) {
                Text(com.example.norsos.localization.AppStrings.save(lang), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(com.example.norsos.localization.AppStrings.cancel(lang))
            }
        }
    )
}
