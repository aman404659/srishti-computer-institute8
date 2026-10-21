package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.NeonCyan

/**
 * Unified, professional institute logo component for Srishti Computer Institute.
 * Uses the official user-provided Srishti Computer Institute emblem logo.
 * Consistently used across:
 * - Home page hero & top
 * - Navigation / Top App Bar
 * - Login screens (Admin & Student)
 * - Admission application form
 * - Student profile & dashboard
 * - Official documents (ID card, Fee receipt, Certificate)
 */
@Composable
fun InstituteLogo(
    size: Dp = 48.dp,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
    isCircular: Boolean = true,
    showBorder: Boolean = true,
    showGlow: Boolean = true
) {
    val context = LocalContext.current

    val logoResId = remember(context) {
        val srishtiId = context.resources.getIdentifier("srishti_logo", "drawable", context.packageName)
        if (srishtiId != 0) return@remember srishtiId
        val instituteId = context.resources.getIdentifier("institute_logo", "drawable", context.packageName)
        if (instituteId != 0) return@remember instituteId
        R.drawable.ic_institute_logo
    }

    val shape: Shape = if (isCircular) CircleShape else RoundedCornerShape(cornerRadius)

    Box(
        modifier = modifier
            .size(size)
            .then(
                if (showGlow) {
                    Modifier.shadow(elevation = 6.dp, shape = shape, spotColor = NeonCyan)
                } else Modifier
            )
            .clip(shape)
            .background(Color.White)
            .then(
                if (showBorder) {
                    Modifier.border(1.5.dp, NeonCyan, shape)
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = logoResId),
            contentDescription = "Srishti Computer Institute Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}
