package com.jeanloth.project.android.kotlin.axounaut.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.jeanloth.project.android.kotlin.axounaut.Constants
import com.jeanloth.project.android.kotlin.axounaut.MainActivity
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.axounaut.theme.*
import com.jeanloth.project.android.kotlin.axounaut.viewModels.MainVM
import com.jeanloth.project.android.kotlin.axounaut.workers.AxounautScheduler
import com.jeanloth.project.android.kotlin.domain_models.entities.AppMessage
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.MessagePriority
import com.jeanloth.project.android.kotlin.domain_models.entities.MessageType
import org.koin.android.viewmodel.ext.android.viewModel

class HomeComposeActivity : ComponentActivity() {

    private val mainVM: MainVM by viewModel()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AxounautScheduler.launchNotificationWorker(this)

        setContent {
            val navController = rememberAnimatedNavController()

            val springSpec = spring<IntOffset>(dampingRatio = Spring.DampingRatioMediumBouncy)
            val tweenSpec = tween<IntOffset>(durationMillis = 2000, easing = CubicBezierEasing(0.08f,0.93f,0.68f,1.27f))

            AnimatedNavHost(navController = navController, startDestination = "home") {
                composable(
                    route = "home",
                    enterTransition = { initial, _ ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = springSpec)
                    },
                    exitTransition = { _, target ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tweenSpec)
                    },
                    popEnterTransition = { initial, _ ->
                        slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = springSpec)
                    },
                    popExitTransition = { _, target ->
                        slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = springSpec)
                    }) { HomePage(navController = navController, mainVM) }
                composable("main") { MainActivity() }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomePage(navController: NavController?, mainVM: MainVM) {
    val context = LocalContext.current

    var messageCenterIsOpenState = remember { mutableStateOf(false) }

    AxounautTheme {
        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(visible = !messageCenterIsOpenState.value) {
                    FloatingActionButton(
                        onClick = {
                            messageCenterIsOpenState.value = true
                        },
                        backgroundColor = gray_light,
                        contentColor = ginger
                    ) {
                        Icon(Icons.Filled.Notifications, "")
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .background(orange_light)
                    .fillMaxSize()
            ) {
                Header(mainVM)
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    HomeBarItem(context)
                }
                val messages = listOf(
                    AppMessage(0, "X Commandes restantes"),
                    AppMessage(0, "Courses à faire !", MessageType.REMINDER),
                    AppMessage(0, "Inventaire à vérifier", MessageType.STOCK)
                )
                AnimatedVisibility(visible = messageCenterIsOpenState.value) {
                    MessageCenter(messages){
                        messageCenterIsOpenState.value = false
                    }
                }
            }
        }

    }
}

@Composable
fun Header(mainVM : MainVM) {

    val totalCommandsCount by mainVM.allCommandCountLiveData().observeAsState()
    val caState by mainVM.caLiveData().observeAsState()
    val unPayedCommandSum by mainVM.unPayedCommandSumLiveData().observeAsState()
    val allArticleData by mainVM.allArticleLiveData().observeAsState()

    Surface(
        color = MaterialTheme.colors.background,
        shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp),
        elevation = 20.dp,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxHeight(0.25f)
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_kb),
                    contentDescription = "Logo Application",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
            ) {
                RoundedCounter(totalCommandsCount.toString(), "Total commandes")
                RoundedCounter(stringResource(id = R.string.price_euro, caState.toString()), "CA")
                RoundedCounter(stringResource(id = R.string.price_euro, unPayedCommandSum.toString()), "Non payés")
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val liste = allArticleData ?: emptyList()
                items(liste.sortedByDescending { it.timeOrdered }) { item ->
                    ArticleBar(item, liste.map { it.timeOrdered }.sum())
                }
            }
        }
    }
}

@Composable
fun RoundedCounter(count: String, description: String, isCircle: Boolean = false) {
    Box(
        modifier = Modifier
            .clip(if (isCircle) CircleShape else RoundedCornerShape(35.dp))
            .background(gray_0)
            .padding(start = 18.dp, end = 18.dp, top = 10.dp, bottom = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = description, fontSize = 8.sp, fontFamily = Typography.body1.fontFamily)
            Text(
                text = count,
                color = ginger,
                fontSize = 25.sp,
                fontFamily = Typography.body1.fontFamily
            )
        }
    }
}

@Composable
fun ArticleBar(article: Article, totalCount: Int = 100) {
    val count = (article.timeOrdered) / totalCount.toFloat()
    val boxSize = 65.dp
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceEvenly) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(gray_0)
                .width(boxSize)
                .fillMaxHeight(0.8f)
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(orange_002)
                    .width(boxSize)
                    .height((count * 100).dp)
                    .align(Alignment.BottomCenter)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .requiredWidth(boxSize)
                    .fillMaxHeight(),
            ){
                Text(
                    text = article.timeOrdered.toString(),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Text(
            text = article.label,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.requiredWidth(boxSize),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun HomeBarItem(context : Context){
    Card(
        shape = RoundedCornerShape(26.dp),
        elevation = 15.dp,
        backgroundColor = white,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp)
        ) {
            // Go to commands
            HomeRoundedCard(Icons.Rounded.List) {
                context.startActivity(Intent(context, MainActivity::class.java))
            }

            // Go to articles
            HomeRoundedCard(Icons.Rounded.BakeryDining) {
                context.startActivity(Intent(context, MainActivity::class.java).apply {
                    putExtra(Constants.FRAGMENT_TO_SHOW, Constants.ARTICLE)
                })
            }

            // Go to clients
            HomeRoundedCard(Icons.Rounded.People) {
                context.startActivity(Intent(context, MainActivity::class.java).apply {
                    putExtra(Constants.FRAGMENT_TO_SHOW, Constants.CLIENTS)
                })
            }

            // Go to stock
            HomeRoundedCard(Icons.Rounded.Equalizer) {
                context.startActivity(Intent(context, MainActivity::class.java).apply {
                    putExtra(Constants.FRAGMENT_TO_SHOW, Constants.STOCK)
                })
            }
        }
    }
}

@Composable
fun HomeRoundedCard(icon: ImageVector, onClickAction: (() -> Unit)){
    Card(
        shape = RoundedCornerShape(15.dp),
        elevation = 5.dp,
        backgroundColor = gray_light,
        modifier = Modifier
    ){
        Icon(icon, modifier = Modifier
            .clickable { onClickAction.invoke() }
            .padding(10.dp), contentDescription = "Localized description")
    }
}

@Composable
fun MessageCenter(
    messages: List<AppMessage>,
    onMessageClick : (() -> Unit)
){
    Card(
        shape = RoundedCornerShape(26.dp),
        elevation = 15.dp,
        backgroundColor = white,
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .fillMaxWidth()
            .clickable {
                onMessageClick.invoke()
            }
    ){
        LazyColumn(
            Modifier.padding(vertical = 5.dp)
        ) {
            itemsIndexed(messages) { index, message ->
                MessageItem(message, index == messages.size - 1)
            }
        }
    }
}

@Composable
fun MessageItem( message: AppMessage, isLastIndex : Boolean = false){
    val icon = when(message.type){
        MessageType.COMMAND -> Icons.Rounded.Sort
        MessageType.REMINDER -> Icons.Rounded.DateRange
        MessageType.STOCK -> Icons.Rounded.Inventory2
    }

    val color = when(message.type.priority){
        MessagePriority.LOW -> green_app
        MessagePriority.MEDIUM -> Color.Yellow
        MessagePriority.HIGH -> Color.Red
    }
    Column {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(icon, modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color)
                .padding(6.dp), contentDescription = "Message icon")
            Text(message.message,
                Modifier
                    .weight(1f)
                    .padding(start = 15.dp), textAlign = TextAlign.Start, fontSize = 10.sp )
            Icon(
                Icons.Rounded.DoubleArrow,
                modifier = Modifier
                    .rotate(90f)
                    .size(32.dp)
                    .padding(6.dp), contentDescription = "Message icon"
            )

        }
        if(!isLastIndex) {
            Canvas(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)) {
                val canvasWidth = size.width
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                drawLine(
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = canvasWidth, y = 0f),
                    color = Color.LightGray,
                    strokeWidth = 1F,
                    pathEffect = pathEffect
                )
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun DefaultPreview() {
    val messages = listOf(
        AppMessage(0, "X Commandes restantes"),
        AppMessage(0, "Courses à faire !", MessageType.REMINDER),
        AppMessage(0, "Inventaire à vérifier", MessageType.STOCK)
    )
    //MessageCenter(messages, mutabeStateOf(false))
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun DefaultPreview2() {
    //HomeBarItem(context)
}