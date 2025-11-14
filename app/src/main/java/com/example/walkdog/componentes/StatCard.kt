@Composable
fun StatCard(item: StatItem, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.height(90.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = Color(0xFF7B42F6))
            Text(item.value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(item.label, fontSize = 13.sp, color = Color.Gray)
        }
    }
}
