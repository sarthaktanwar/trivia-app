package com.example.triviaapp.presentation.component



import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.triviaapp.model.QuestionItem

import com.example.triviaapp.presentation.screens.QuestionScreen.QuestionViewModel
import com.example.triviaapp.util.AppColors

@Composable
fun Questions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()

    val questionIndex = remember {
        mutableStateOf(0)
    }
    if(viewModel.data.value.loading == true ){
        CircularProgressIndicator()
        Log.d("Loading","Questions .. Loading .. ")
    }else{

        val question = try{
            questions?.get(questionIndex.value)
        }catch (ex: Exception){
            null
        }
       if(questions != null){
           QuestionDisplay(question = question!!,questionIndex = questionIndex,viewModel = viewModel){
               questionIndex.value = questionIndex.value+1
           }
       }
    }

}



@Composable
fun QuestionDisplay(
    question :QuestionItem,
      questionIndex: MutableState<Int>,
      viewModel: QuestionViewModel,
      onNextClick: (Int)->Unit = {}
){
    val choiceState  = remember(question){
        question.choices.toMutableList()
    }

    val answerState = remember(question) {
        mutableStateOf<Int?>(null)

    }
    val correctAnswerState = remember(question) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int)->Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choiceState[it] == question.answer


        }
    }


    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f,10f),0f)
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = AppColors.mDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            QuestionText(current_number = questionIndex.value +1)
            DrawDottedLine(pathEffect =pathEffect )
            
            Column {
                Text(
                    text = question.question,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxHeight(0.3f),
                    fontSize = 17.sp,
                    color = AppColors.mOffWhite,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp

                )
                choiceState.forEachIndexed{index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .height(45.dp)
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColors.mOffDarkPurple,
                                        AppColors.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topEndPercent = 50,
                                    bottomEndPercent = 50,
                                    topStartPercent = 50,
                                    bottomStartPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        RadioButton(selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor = if(correctAnswerState.value == true && index == answerState.value){
                                    Color.Green.copy(alpha = 0.2f)
                                }else{
                                    Color.Red.copy(alpha = 0.2f)
                                }))

                        val annotatedString = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Light,
                                color = if (correctAnswerState.value == true && index == answerState.value){
                                    Color.Green
                                }else if(correctAnswerState.value == false && index == answerState.value){
                                    Color.Red
                                }else{
                                    AppColors.mOffWhite
                                })){
                                append(answerText)
                            }

                        }
                        Text(text = annotatedString, modifier = Modifier.padding(6.dp))



                    }
                }
                Button(
                    onClick = { onNextClick(questionIndex.value) },
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = AppColors.mLightBlue)) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        color = AppColors.mOffWhite,
                        fontSize = 17.sp
                    )

                }

                
            }
            
            
        }


    }
}

@Preview
@Composable
fun QuestionText(
    current_number: Int = 10 ,
    final_number : Int = 100,
){

    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)){
            withStyle(style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Bold, fontSize = 27.sp)){
                append("Question $current_number/")
                withStyle(style = SpanStyle(color = AppColors.mLightGray, fontWeight = FontWeight.Light, fontSize = 14.sp)){
                    append("$final_number")
                }
            }

    } }, modifier = Modifier.padding(20.dp)
    )
}

@Composable
fun DrawDottedLine(pathEffect:PathEffect){

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)){
        drawLine(
            color = AppColors.mLightGray,
            start  = Offset(0f,0f),
            end = Offset(size.width,0f),
            pathEffect = pathEffect
        )

    }

}

@Preview
@Composable
fun ShowProgress(score: Int = 12){

    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075),Color(0xFFBE6BE5)))

    val progressFactor  = remember {

    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.mLightPurple,
                        AppColors.mLightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent,

            )
        ) {
            
        }

    }




}