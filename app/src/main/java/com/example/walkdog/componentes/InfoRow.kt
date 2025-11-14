@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(label, fontWeight = FontWeight.Medium)
        Spacer(Modifier.width(4.dp))
        Text(value)
    }
}
