package com.sample.restaurantordertakingapp.ui.theme.screen.menu_details

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.sample.restaurantordertakingapp.domain.model.CartItem
import com.sample.restaurantordertakingapp.data.model.MenuItem
import com.sample.restaurantordertakingapp.data.model.OrderItem
import com.sample.restaurantordertakingapp.ui.theme.component.common.QuantitySelector
import com.sample.restaurantordertakingapp.utils.NetworkImage

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemDetailScreen1(modifier: Modifier, menuItem: MenuItem, closeSheet : () -> Unit, addToCart : (CartItem) -> Unit) {
    ModalBottomSheet(onDismissRequest = {
        closeSheet()
    }) {
        var quantity by remember { mutableIntStateOf(1) }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .fillMaxHeight()
        ) {
            NetworkImage(
                menuItem.imageUrl,
                menuItem.name,
                modifier = Modifier.fillMaxWidth().aspectRatio(2f)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    menuItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    menuItem.getFormattedPrice(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("${menuItem.description}", style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(horizontal = 8.dp, 16.dp))
            Card ( shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()
                ) {
                    QuantitySelector(quantity= quantity, onQuantityChange = {
                        quantity = it
                    }, showLabel = true, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
            }
            var isFull by remember { mutableStateOf(true) }
            Spacer(Modifier.height(16.dp))
            RadioButtonGroupWithBorder(onClick = {isFullSelected ->
                isFull = isFullSelected
            })

            var selectedTable by remember { mutableStateOf<String?>(null) }
            var tableList  = (1..6).map { "Table $it" }
            TableChipsRow(tableList, selectedTable ,{
                selectedTable = it
            })
            Spacer(Modifier.height(16.dp))
            Button(onClick = {
                val cartItem = CartItem(id= menuItem.id, name   =menuItem.name, quantity =quantity, isFull =  isFull, price = menuItem.price, table = selectedTable, imageUrl = menuItem.imageUrl, takeAway = (selectedTable=="Takeaway") )
                addToCart(cartItem)
            }, modifier = Modifier.fillMaxWidth().align(Alignment.End)) {
                Text("Add to Cart", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
*/


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemDetailScreen1(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    closeSheet: () -> Unit,
    addToCart: @Composable (CartItem) -> Unit
) {

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()


    var quantity by remember { mutableIntStateOf(1) }
    var isFull by remember { mutableStateOf(true) }
    var selectedTable by remember { mutableStateOf<String?>(null) }
    val tableList = (1..6).map { "Table $it" }

    ModalBottomSheet(
        onDismissRequest = closeSheet,
        sheetState = sheetState,
        modifier = modifier
    ){
        Column(
            modifier = modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            NetworkImage(
                menuItem.imageUrl,
                menuItem.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    menuItem.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    menuItem.getFormattedPrice(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            // fixed padding call
            menuItem.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
                )
            }

            Card(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                QuantitySelector(
                    quantity = quantity,
                    onQuantityChange = { quantity = it },
                    showLabel = true,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            RadioButtonGroupWithBorder(onClick = { isFull = it })

            Spacer(Modifier.height(12.dp))

            TableChipsRow(tableList, selectedTable) { selectedTable = it }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    val cartItem = CartItem(
                        id = menuItem.id,
                        name = menuItem.name,
                        quantity = quantity,
                        isFull = isFull,
                        price = menuItem.price,
                        table = selectedTable,
                        imageUrl = menuItem.imageUrl,
                        takeAway = (selectedTable == "Takeaway")
                    )
                    //todo sndp
                  ///  addToCart(cartItem)
                }) {
                    Text("Add to Cart", style = MaterialTheme.typography.labelLarge)
                }
            }

            Spacer(Modifier.height(12.dp))

            // optional: a small dismiss button
            TextButton(onClick = { closeSheet() }, modifier = Modifier.align(Alignment.End)) {
                Text("Close")
            }
        }
    }
    // Make content scrollable just in case sheet height is limited

}


@Composable
fun RadioButtonGroupWithBorder(onClick : (isFullSelected : Boolean) -> Unit) {
    val radioOptions = listOf("Half", "Full")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Card ( shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.selectableGroup().padding(horizontal = 8.dp, vertical = 8.dp)) {
            radioOptions.forEach { text ->
                Row(
                    modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp, end = 8.dp)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                                if(text == "Full"){onClick(true)} else { onClick(false)} // Handle clicks on the Row for better accessibility

                            },
                            role = Role.RadioButton
                        )
                        .border(
                            border = BorderStroke(
                                1.dp,
                                if (text == selectedOption) MaterialTheme.colorScheme.primary else Color.Gray
                            ),
                            shape = RoundedCornerShape(8.dp) // Adjust corner radius as needed
                        ),
                    verticalAlignment = Alignment.CenterVertically// Padding inside the border

                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = null,
                        modifier = Modifier.padding(6.dp)
                    )
                    Text(
                        text = text,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuItemDetailScreen(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    closeSheet: () -> Unit,
    addToCart: (OrderItem) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        NetworkImage(
            menuItem.imageUrl,
            menuItem.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
        )
        Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                menuItem.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            menuItem.description?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        var quantity by remember { mutableIntStateOf(1) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = { if (quantity > 1) quantity-- }) {
                Text("−")
            }
            Spacer(Modifier.width(16.dp))
            Text("$quantity", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.width(16.dp))
            Button(onClick = { quantity++ }) {
                Text("+")
            }
        }

        Spacer(Modifier.height(16.dp))
        var isFull by remember { mutableStateOf(true) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = !isFull, onClick = { isFull = false })
            Text("Half", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(16.dp))
            RadioButton(selected = isFull, onClick = { isFull = true })
            Text("Full", style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                val item = OrderItem(
                    id = menuItem.id,
                    quantity = quantity,
                    isFull = isFull,
                    price = menuItem.price,
                    table = "table"
                )
                addToCart(item)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add to Cart")
        }

        Spacer(Modifier.height(32.dp))
        Button(onClick = closeSheet, modifier = Modifier.fillMaxWidth()) {
            Text("Close")
        }
    }
}


@Composable
fun TableChipsRow(
    tables: List<String>,
    selectedTable: String?,
    onSelect: (String?) -> Unit
) {
   /* // horizontal scrollable row of chips
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) */
    FlowRow(mainAxisSpacing = 4.dp,
        crossAxisSpacing = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)) {
        // optional "None / Takeaway" chip
        FilterChip(
            selected = selectedTable == null,
            onClick = { onSelect(null) },
            label = { Text("Takeaway") }
        )

        tables.forEach { table ->
            FilterChip(
                selected = table == selectedTable,
                onClick = {
                    // toggle selection: selecting again clears selection (optional)
                    if (table == selectedTable) onSelect(null) else onSelect(table)
                },
                label = { Text(table) }
            )
        }
    }
}