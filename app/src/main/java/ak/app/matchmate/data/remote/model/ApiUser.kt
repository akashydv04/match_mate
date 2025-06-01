package ak.app.matchmate.data.remote.model

data class ApiUser(
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val dob: Dob,
    val phone: String,
    val cell: String,
    val picture: Picture,
    val login: Login // We need this for the unique UUID
)