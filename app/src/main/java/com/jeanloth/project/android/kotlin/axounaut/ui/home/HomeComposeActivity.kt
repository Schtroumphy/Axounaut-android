package com.jeanloth.project.android.kotlin.axounaut.ui.home

import android.content.Intent
import androidx.compose.ui.tooling.preview.Devices
import com.jeanloth.project.android.kotlin.axounaut.theme.*
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.jeanloth.project.android.kotlin.domain_models.entities.Article

class HomeComposeActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    }) { HomePage(navController = navController) }
                composable("main") { MainActivity() }
            }
        }
    }
}

@Composable
fun HomePage(navController: NavController?) {
    val context = LocalContext.current
    AxounautTheme {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* ... */ },
                    backgroundColor = green_light_1,
                    contentColor = white
                ) {
                    Icon(Icons.Filled.Add, "")
                }
            }
        ) {
            Column(
                modifier = Modifier.background(orange_light)
            ) {
                Header()
                Spacer(modifier = Modifier.height(15.dp))
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                ) {
                    HomeCard("Mes commandes") {
                        context.startActivity(Intent(context, MainActivity::class.java))
                    }
                    HomeCard("Mon stock") {
                        context.startActivity(Intent(context, MainActivity::class.java).apply {
                            putExtra(Constants.FRAGMENT_TO_SHOW, Constants.ARTICLE)
                        })
                    }
                }
                HomeItem()
            }
        }

    }
}

@Composable
fun Header() {
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxHeight(0.3f)
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
                RoundedCounter("45", "Total commandes")
                RoundedCounter("565,0€", "CA")
                RoundedCounter("75,0€", "Réductions")
            }
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val liste = listOf(
                    Article(0L, "Pain au choco", 30.5, 15),
                    Article(0L, "Pomme cannelle", 30.5, 1),
                    Article(0L, "Rolls Kanèls", 30.5, 42),
                    Article(0L, "Chocolat", 30.5, 65),
                    Article(0L, "Flan au coco", 30.5, 20),
                    Article(0L, "Petits pains", 30.5, 15),
                    Article(0L, "Petits pains fourrés boeuf", 30.5, 15),
                )

                items(liste.sortedByDescending { it.timeOrdered }) { item ->
                    ArticleBar(item)
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
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                .background(gray_0)
                .width(boxSize)
                .fillMaxHeight(0.9f)
        ) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp))
                    .background(orange_002)
                    .width(boxSize)
                    .height((count * 100).dp)
                    .align(Alignment.BottomCenter)
            )
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
fun HomeCard(title: String, onClickAction: (() -> Unit)) {
    Card(
        shape = RoundedCornerShape(18.dp),
        elevation = 15.dp,
        backgroundColor = white,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClickAction.invoke() }

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontFamily = Typography.body1.fontFamily,
                modifier = Modifier.padding(15.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_right_arrow),
                contentDescription = "Right arrow",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 15.dp)
            )

        }
    }
}

@Composable
fun HomeItem() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(blue_001)
    ) {
        // TODO
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_2)
@Composable
fun DefaultPreview() {
    HomePage(navController = null)
}