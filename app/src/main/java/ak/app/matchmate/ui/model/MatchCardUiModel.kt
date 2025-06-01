package ak.app.matchmate.ui.model

data class MatchCardUiModel(
    val uuid: String,
    val fullName: String,
    val details: String, // e.g., "Age, City"
    val imageUrl: String,
    val matchScoreText: String, // e.g., "Match: 85%"
    val education: String,
    val religion: String,
    val status: String, // "PENDING", "ACCEPTED", "DECLINED"
    val showActions: Boolean // true if status is PENDING, false otherwise
)