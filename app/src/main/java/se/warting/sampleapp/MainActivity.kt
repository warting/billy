package se.warting.sampleapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import se.warting.sampleapp.advance.AdvanceActivity
import se.warting.sampleapp.compose.ComposeActivity
import se.warting.sampleapp.ui.theme.PerfectAndroidLibraryTemplateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PerfectAndroidLibraryTemplateTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Button(onClick = {
                            start(AdvanceActivity::class.java)
                        }) {
                            Text("Advance Sample")
                        }
                        Button(onClick = {
                            start(ComposeActivity::class.java)
                        }) {
                            Text("Compose Sample")
                        }
                    }
                }
            }
        }
    }

    private fun start(clazz: Class<*>) {
        startActivity(Intent(this@MainActivity, clazz))
    }
}
