package com.ntech.theyardhub.feature.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.RouteName
import com.ntech.theyardhub.core.RouteName.CHAT_LIST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_POST_SCREEN
import com.ntech.theyardhub.core.RouteName.DETAIL_YARD_SCREEN
import com.ntech.theyardhub.core.RouteName.GROUP_CHAT_SCREEN
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Gray
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.core.theme.bluePrimary
import com.ntech.theyardhub.core.utils.AppResponse
import com.ntech.theyardhub.core.utils.RoundedImageExample
import com.ntech.theyardhub.datalayer.model.PostModel
import com.ntech.theyardhub.datalayer.model.YardModel
import com.ntech.theyardhub.feature.main.MainActivityViewModel
import org.koin.androidx.compose.get

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    onSeeAllYardClicked: () -> Unit,
    onSeeAllArticleClicked: () -> Unit,
) {
    val itemsList = arrayListOf<PostModel>()
    val yardList = arrayListOf<YardModel>()

    val viewModel: HomeViewModel = get()
    val mainActivityViewModel: MainActivityViewModel = get()

    LaunchedEffect(Unit) {
        viewModel.fetchPosts()
        viewModel.fetchYards()
    }

    val postState = viewModel.postLiveData.observeAsState().value
    val yardState = viewModel.yardLiveData.observeAsState().value

    if (postState is AppResponse.Success) {
        itemsList.addAll(postState.data.filterNot { it in itemsList })
    }

    if (yardState is AppResponse.Success) {
        yardList.addAll(yardState.data.filterNot { it in yardList })
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val image: Painter = painterResource(id = R.drawable.icon)
                        val sendIcon: Painter = painterResource(id = R.drawable.send)
                        Image(
                            painter = image,
                            contentDescription = "App Logo",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "YardHub",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .weight(1f),
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
                        )
                        Image(
                            painter = sendIcon,
                            contentDescription = "Send Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    navController.navigate(
                                        GROUP_CHAT_SCREEN
                                    )
                                }
                        )
                        Image(
                            painter = sendIcon,
                            contentDescription = "Send Icon",
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    navController.navigate(
                                        CHAT_LIST_SCREEN
                                    )
                                }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                )
            )
        },
        containerColor = White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding() + 24.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
        ) {
            HeaderHome(if (viewModel.getIsGuest()) "Guest" else viewModel.userName)
            Spacer(modifier = Modifier.height(32.dp))
            ProductHome(
                yardList,
                onSeeAllClicked = {
                    onSeeAllYardClicked.invoke()
                    navController.navigate(RouteName.YARD_SCREEN)
                },
                onClickItem = {
                    navController.navigate("$DETAIL_YARD_SCREEN/${it.documentId}")
                }
            )
            Spacer(modifier = Modifier.height(32.dp))
            ArticleHome(
                itemsList,
                onSeeAllClicked = {
                    onSeeAllArticleClicked.invoke()
                    navController.navigate(RouteName.POST_SCREEN)
                },
                onClickItem = {
                    navController.navigate("$DETAIL_POST_SCREEN/${it.documentId}")
                }
            )
        }
    }
}

@Composable
fun HeaderHome(name: String) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Column {
            Text(text = "Hello " + name, style = Typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We bring the best for you",
                style = Typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = bluePrimary
                )
            )
        }
        RoundedImageExample()
    }
}

@Composable
fun ArticleHome(
    itemsList: ArrayList<PostModel>,
    onSeeAllClicked: () -> Unit,
    onClickItem: (PostModel) -> Unit,

    ) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Article for You", style = Typography.titleMedium)
            Text(
                text = "See all",
                style = Typography.labelSmall,
                modifier = Modifier.clickable { onSeeAllClicked.invoke() })
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            items(itemsList.size) { item ->
                ArticleHomeItem(
                    post = itemsList[item],
                    onClickItem = { onClickItem.invoke(it) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ProductHome(
    yardList: ArrayList<YardModel>,
    onSeeAllClicked: () -> Unit,
    onClickItem: (YardModel) -> Unit,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Farms", style = Typography.titleMedium)
            Text(
                text = "See all",
                style = Typography.labelSmall,
                modifier = Modifier.clickable { onSeeAllClicked.invoke() })
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(yardList.size) { index ->
                YardHomeItem(item = yardList[index], onClickItem = { onClickItem.invoke(it) })
            }
        }
    }
}

@Composable
fun ArticleHomeItem(
    post: PostModel = PostModel(), onClickItem: (PostModel) -> Unit,
) {
    Box(
        modifier = Modifier
            .border(
                BorderStroke(0.5.dp, Gray),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
            .clickable {
                onClickItem.invoke(post)
            }
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImage(
                model = "https://images-squarespace--cdn-com.translate.goog/content/v1/552ed2d1e4b0745abca6723d/3e60e68a-5ee9-4f49-9261-e890a6673173/grape+3.jpg?format=2500w&_x_tr_sl=en&_x_tr_tl=id&_x_tr_hl=id&_x_tr_pto=tc",
                contentDescription = "Image from URL",
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillHeight
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
            ) {
                Text(text = post.title, style = Typography.titleSmall)
                Text(
                    text = post.content,
                    style = Typography.labelSmall.copy(color = Gray),
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun YardHomeItem(item: YardModel, onClickItem: (YardModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(White),
        modifier = Modifier
            .clickable { onClickItem.invoke(item) }
            .width(128.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = item.thumbnail,
                contentDescription = "Image from URL",
                modifier = Modifier
                    .height(128.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color = Color.Black),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = item.name, style = Typography.titleSmall.copy(
                        color = Black, fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.locationModel.city,
                    modifier = Modifier.padding(top = 5.dp),
                    style = Typography.bodyMedium
                )
            }
        }
    }
}
