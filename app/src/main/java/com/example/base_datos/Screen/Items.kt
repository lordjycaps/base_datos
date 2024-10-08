package com.example.base_datos.Screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.base_datos.DAO.UserDAO
import com.example.base_datos.Model.User
import com.example.base_datos.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun UserApp(userRepository: UserRepository) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var edad by rememberSaveable { mutableStateOf("") }
    var id by rememberSaveable { mutableStateOf("") }
    var scope = rememberCoroutineScope()
    var context = LocalContext.current

    // Preservar la lista de usuarios
    var users by rememberSaveable { mutableStateOf(listOf<User>()) }
    var isEditMode by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var userToDelete by rememberSaveable { mutableStateOf<User?>(null) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título centrado
        Text(
            text = "Registro de Usuario",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Campos de texto
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text(text = "Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text(text = "Apellido") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = edad,
            onValueChange = { edad = it },
            label = { Text(text = "Edad") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de registrar o editar, centrado
        Button(
            onClick = {
                if (nombre.isEmpty() || apellido.isEmpty() || edad.isEmpty()) {
                    Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(
                        id = if (id.isNotEmpty()) id.toInt() else 0,
                        nombre = nombre,
                        apellido = apellido,
                        edad = edad.toIntOrNull() ?: 0
                    )
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            if (isEditMode) {
                                userRepository.updateUser(user)
                                isEditMode = false
                            } else {
                                userRepository.insert(user)
                            }
                        }
                        Toast.makeText(
                            context,
                            if (isEditMode) "Usuario Actualizado" else "Usuario Registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                        clearFields(
                            onClear = {
                                nombre = ""
                                apellido = ""
                                edad = ""
                                id = ""
                            }
                        )
                        users = withContext(Dispatchers.IO) {
                            userRepository.getAllUser()
                        }
                    }
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = if (isEditMode) "Actualizar" else "Registrar")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón para cancelar la edición
        if (isEditMode) {
            Button(
                onClick = {
                    clearFields(
                        onClear = {
                            nombre = ""
                            apellido = ""
                            edad = ""
                            id = ""
                        }
                    )
                    isEditMode = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Cancelar", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de usuarios con scroll
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1f) // Ocupa el espacio restante con scroll
        ) {
            items(users) { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "ID: ${user.id}")
                            Text(text = "Nombre: ${user.nombre}")
                            Text(text = "Apellido: ${user.apellido}")
                            Text(text = "Edad: ${user.edad}")
                        }
                        Row {
                            // Icono para editar
                            IconButton(onClick = {
                                nombre = user.nombre
                                apellido = user.apellido
                                edad = user.edad.toString()
                                id = user.id.toString()
                                isEditMode = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar",
                                    tint = Color.Blue
                                )
                            }

                            // Icono para borrar
                            IconButton(onClick = {
                                userToDelete = user
                                showDeleteDialog = true
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Borrar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación para eliminar
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Confirmar Eliminación") },
                text = { Text(text = "¿Estás seguro de que deseas eliminar este usuario?") },
                confirmButton = {
                    Button(
                        onClick = {
                            userToDelete?.let { user ->
                                scope.launch {
                                    withContext(Dispatchers.IO) {
                                        userRepository.deleteById(user.id)
                                    }
                                    Toast.makeText(context, "Usuario Eliminado", Toast.LENGTH_SHORT).show()
                                    users = withContext(Dispatchers.IO) {
                                        userRepository.getAllUser()
                                    }
                                }
                            }
                            showDeleteDialog = false
                        }
                    ) {
                        Text(text = "Eliminar")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text(text = "Cancelar")
                    }
                }
            )
        }
    }
}

fun clearFields(onClear: () -> Unit) {
    onClear()
}