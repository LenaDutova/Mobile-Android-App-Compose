package com.mobile.vedroid.compose.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.mobile.vedroid.compose.R
import com.mobile.vedroid.compose.ui.theme.MobileAndroidAppComposeTheme

@Composable
fun ItemJoke (isSingle: Boolean = true,
              setup: String = stringResource(R.string.lorem_ipsum),
              delivery: String? = null){

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),

        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ConstraintLayout(modifier = Modifier
            .padding(16.dp)
        ) {
            val (firstText, secondText, smile) = createRefs()

            if (!isSingle) {
                Text(
                    text = setup,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,

                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .constrainAs(firstText) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)

                        }
                )
            }

            Image(
                painter = painterResource(R.drawable.smile),
                contentDescription = "smile",

                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiaryContainer),

                modifier = Modifier
                    .size(32.dp)
                    .constrainAs(smile){
                        top.linkTo(firstText.bottom)
                        absoluteLeft.linkTo(parent.start)
                    }
            )

            Text(text = if (isSingle) setup else delivery!!,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .constrainAs(secondText){
                        top.linkTo(firstText.bottom)
                        start.linkTo(smile.end, margin = 16.dp)
                        end.linkTo(parent.end)

                        width = Dimension.preferredWrapContent
                    }
                    .fillMaxWidth()
            )

        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "Light"
)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Dark"
)
@Composable
private fun PreviewItemJoke (){
    MobileAndroidAppComposeTheme (dynamicColor = false) {
        ItemJoke()
//        ItemJoke(true, "Как пропатчить KDE под FreBSD?")
//        ItemJoke(false,"Почему ваши дети всё время ссорятся?", "Конфликт версий")
    }
}