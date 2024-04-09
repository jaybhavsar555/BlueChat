package com.example.bluechat.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bluechat.MainActivity
import com.example.bluechat.R


class FirstScreenActivity() : ComponentActivity() {



    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customBlue = Color(0xFF4D87F9)
        setContent {

            Column(
                modifier = Modifier
                    .fillMaxSize() // Optional: Fills the entire screen
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(506.dp)

                        .background(
                            Color(0xFF4D87F9)
                        ) // Takes half of the available height
                ) {
                    // Image content
                    Image(
                        painter = painterResource(R.drawable.img), // Replace with your image resource ID
                        contentDescription = "Image Description",
                        modifier = Modifier.fillMaxSize() // Fills the entire box
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(337.dp)

                    // Takes half of the available height
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize().width(200.dp).padding(vertical = 20.dp, horizontal = 25.dp), // Fills the entire screen
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start

                    ) {


                            Text(
                                text = "Let’s connect on the Bluechat",
                                modifier = Modifier.weight(2f), // Occupies 3/4th of the available height
                                fontSize = 35.sp, // Adjust font size as needed
                                fontWeight = FontWeight.Bold// Makes the text bold
//                         fontFamily =  FontDmaily.
                            )


                        Text(
                            text = "Experience the magic of chatting offline 😍",
                            modifier = Modifier.weight(2f), // Occupies 1/4th of the available height
                            fontSize = 13.sp, // Adjust font size as needed
                        )

                        Button(
                            onClick = {
                            val navigate =
                                Intent(this@FirstScreenActivity, MainActivity::class.java)
                            startActivity(navigate)
                        },
                            modifier = Modifier.fillMaxWidth(0.5f),
                            colors = ButtonDefaults.buttonColors(customBlue)

                        )
                        {
                            Text("Start")

                        }

                    }
                }
            }


        }

    }

}
