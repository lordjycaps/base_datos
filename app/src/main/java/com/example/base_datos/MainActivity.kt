package com.example.base_datos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.base_datos.DAO.UserDAO
import com.example.base_datos.Database.UserDatabase
import com.example.base_datos.Repository.UserRepository
import com.example.base_datos.Screen.UserApp
import com.example.base_datos.ui.theme.Base_DatosTheme

class MainActivity : ComponentActivity() {
    //lateinit evita el nulo ya que la variable se debe inicializar mas adelante
    private lateinit var userRepository: UserRepository
    private lateinit var userDAO: UserDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = UserDatabase.getDatabase(applicationContext)
        userDAO= db.userDao()
        userRepository = UserRepository(userDAO)
        enableEdgeToEdge()
        setContent {
            UserApp(userRepository)
        }
    }
}
