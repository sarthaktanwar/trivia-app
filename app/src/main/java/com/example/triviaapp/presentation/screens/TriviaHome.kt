package com.example.triviaapp.presentation.screens

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

import com.example.triviaapp.presentation.component.Questions
import com.example.triviaapp.presentation.screens.QuestionScreen.QuestionViewModel


@Composable
fun TreviaHome(viewModel: QuestionViewModel = hiltViewModel()){
    Questions(viewModel)
}
