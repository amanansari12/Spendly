package com.amanansari.spendly.onBoarding.screen

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amanansari.spendly.R
import com.amanansari.spendly.data.local.entity.UserEntity
import com.amanansari.spendly.onBoarding.viewmodel.OnboardingStep
import com.amanansari.spendly.onBoarding.viewmodel.UserViewModel
import com.amanansari.spendly.ui.theme.LightNavInactive
import com.amanansari.spendly.ui.theme.LightTextSecondary
import com.amanansari.spendly.ui.theme.Primary

@Composable
fun OnboardingScreen(userViewModel: UserViewModel) {

    val isOnboardingCompleted by userViewModel.isOnboardingCompleted.collectAsState()
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(userViewModel._email).matches()

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
        ){

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center

        ){
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "spendly_logo",
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Spendly",
                fontWeight = FontWeight.Bold,
                fontSize = 35.sp
            )
            Text(text = "Track every rupee. Own your finances.",
                fontSize = 15.sp,
                color = LightTextSecondary
            )
        }

        Spacer(modifier = Modifier.height(10.dp)        )

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ){

            when(userViewModel.currentStep){
                OnboardingStep.NAME -> {
                    Text(text = "YOUR NAME",
                        color = LightNavInactive,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    OutlinedTextField(
                        value = userViewModel._name ?: "",
                        onValueChange = { input ->
                            if(input.all { it.isLetter() || it.isWhitespace() }){
                                userViewModel.updateName(input)
                            }
                        },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        placeholder = {
                            Text("Please Enter Your Name", color = Color.Gray)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        label = {Text("Name")},
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray
                        )
                    )



                    Button(
                        onClick = { userViewModel.goToNextStep() },
                        enabled = userViewModel._name.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary, // background color

                            contentColor = Color.White,

                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.DarkGray,
                        )
                    ) {
                        Text(
                            text = "Next",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                OnboardingStep.EMAIL -> {
                    Text(text = "YOUR EMAIL",
                        color = LightNavInactive,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    OutlinedTextField(
                        value = userViewModel._email ?: "",
                        onValueChange = { userViewModel.updateEmail(it) },
                        textStyle = LocalTextStyle.current.copy(color = Color.Black),
                        placeholder = {
                            Text("Please Enter Your Email", color = Color.Gray)
                        },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        isError = userViewModel._email.isNotBlank() && !isEmailValid,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedLabelColor = Color.LightGray
                        )
                    )

                    Button(onClick = {
                        userViewModel.insertUser(
                            user = UserEntity(
                                name = userViewModel._name,
                                email = userViewModel._email,
                            )
                        )

                        userViewModel.completeOnboarding()
                    },
                        enabled = isEmailValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary, // background color
                            contentColor = Color.White,

                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.DarkGray,
                        ),
                    ) {
                        Text(
                            text = "Get Started ",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                }

            }

        }

            Spacer(modifier = Modifier.weight(1f))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "",
                    tint = LightNavInactive,
                    modifier = Modifier.size(15.dp)
                )
                Text(text = "Your data stays on your device. Always.",
                    fontSize = 15.sp,
                    color = LightNavInactive
                )
            }

            Spacer(modifier = Modifier.height(10.dp))



        }




    }


@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview(){
//    OnboardingScreen()
}
