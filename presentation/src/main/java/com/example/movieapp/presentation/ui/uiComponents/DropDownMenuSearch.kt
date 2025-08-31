package com.example.movieapp.presentation.ui.uiComponents

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownMenuSeacrh(
    modifier: Modifier = Modifier,
    list: List<String>,
    label: String,
    placeholder: String,
    onClick: (String) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    var text by remember {
        mutableStateOf("")
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = text,
            onValueChange = { },
            placeholder = {
                Text(text = placeholder)
            },
            label = {
                Text(text = label)
            },
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) }
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            list.forEachIndexed { index, type ->
                DropdownMenuItem(
                    text = {
                        Text(text = type)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        text = list[index]
                        isExpanded = false
                        onClick(list[index])
                    },
                )
            }
        }
    }
}