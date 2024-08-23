package com.ntech.theyardhub.feature.product

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ntech.theyardhub.R
import com.ntech.theyardhub.core.theme.Black
import com.ntech.theyardhub.core.theme.Typography
import com.ntech.theyardhub.core.theme.White
import com.ntech.theyardhub.datalayer.model.ProductModel

@Composable
fun ProductItem(product: ProductModel, onClickItem: (ProductModel) -> Unit) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(White),
        modifier = Modifier
            .clickable { onClickItem.invoke(product) }
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.size(42.dp),
                painter = painterResource(R.drawable.icon),
                contentDescription = "avatar",
                contentScale = ContentScale.FillBounds
            )
            Text(
                text = product.name, style = Typography.titleMedium.copy(
                    color = Black, fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = product.price.toString(),
                modifier = Modifier.padding(top = 5.dp),
                style = Typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostItemPreview() {
    ProductItem(product = ProductModel(), onClickItem = {})
}