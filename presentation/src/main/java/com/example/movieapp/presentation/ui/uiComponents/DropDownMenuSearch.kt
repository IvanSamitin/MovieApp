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
import com.example.movieapp.domain.model.Order
import com.example.movieapp.domain.model.Type
import com.example.movieapp.presentation.util.asUiText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T :Enum<T>> DropDownMenuSeacrh(
    list: List<T>,
    label: String? = null,
    placeholder: String? = null,
    text: String?,
    onClick: (T) -> Unit,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            value = text ?: "",
            onValueChange = { },
            placeholder = {
                placeholder?.let {
                    Text(text = it)
                }
            },
            label = {
                label?.let {
                    Text(text = it)
                }
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
                        when(type){
                            is Order ->{
                                Text(text = type.asUiText().asString())
                            }
                            is Type ->{
                                Text(text = type.asUiText().asString())
                            }
                        }
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    onClick = {
                        onClick(list[index])
                        isExpanded = false
                    },
                )
            }
        }
    }
}