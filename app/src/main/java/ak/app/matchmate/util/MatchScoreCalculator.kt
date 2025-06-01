package ak.app.matchmate.util

import kotlin.math.abs

object MatchScoreCalculator {


    fun calculateScore(
        currentUserAge: Int,
        matchAge: Int,
        currentUserCity: String,
        matchCity: String
    ): Int {
        var score = 0


        val ageDifference = abs(currentUserAge - matchAge)
        score += when {
            ageDifference == 0 -> 60 // Perfect age match
            ageDifference <= 2 -> 50 // Within 2 years
            ageDifference <= 5 -> 40 // Within 5 years
            ageDifference <= 10 -> 20 // Within 10 years
            else -> 0 // More than 10 years difference
        }


        if (currentUserCity.equals(matchCity, ignoreCase = true)) {
            score += 40
        }


        return score.coerceIn(0, 100)
    }


    val CURRENT_USER_SIMULATED_AGE = 30
    val CURRENT_USER_SIMULATED_CITY = "London" // A common city for randomuser.me
}