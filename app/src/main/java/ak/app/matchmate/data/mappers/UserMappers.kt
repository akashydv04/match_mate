package ak.app.matchmate.data.mappers

import ak.app.matchmate.data.local.model.UserEntity
import ak.app.matchmate.data.remote.model.ApiUser
import ak.app.matchmate.ui.model.MatchCardUiModel
import ak.app.matchmate.util.MatchScoreCalculator
import kotlin.random.Random

object UserMappers {
    private val EDUCATION_LIST = listOf(
        "High School Diploma",
        "Bachelor's Degree",
        "Master's Degree",
        "Ph.D.",
        "Vocational Training",
        "Associate's Degree"
    )

    private val RELIGION_LIST = listOf(
        "Hindu",
        "Christian",
        "Muslim",
        "Sikh",
        "Buddhist",
        "Agnostic",
        "Atheist"
    )

    fun ApiUser.toUserEntity(): UserEntity {
        // Generate random education and religion for simulation
        val randomEducation = EDUCATION_LIST.random(Random)
        val randomReligion = RELIGION_LIST.random(Random)

        // Calculate match score based on simulated current user's age and city
        // In a real application, currentUserAge and currentUserCity would come
        // from the actual logged-in user's profile.
        val matchScore = MatchScoreCalculator.calculateScore(
            currentUserAge = MatchScoreCalculator.CURRENT_USER_SIMULATED_AGE,
            matchAge = this.dob.age,
            currentUserCity = MatchScoreCalculator.CURRENT_USER_SIMULATED_CITY,
            matchCity = this.location.city
        )

        return UserEntity(
            uuid = this.login.uuid,
            firstName = this.name.first,
            lastName = this.name.last,
            age = this.dob.age,
            city = this.location.city,
            imageUrl = this.picture.large, // Use large image URL
            matchScore = matchScore,
            status = "PENDING", // Initial status for all fetched users
            education = randomEducation,
            religion = randomReligion
        )
    }

    fun UserEntity.toMatchCardUiModel(): MatchCardUiModel {
        val fullName = "${this.firstName} ${this.lastName}"
        val details = "${this.age}, ${this.city}"
        val matchScoreFormatted = "Match: ${this.matchScore}%"
        val showActionButtons = this.status == "PENDING"

        return MatchCardUiModel(
            uuid = this.uuid,
            fullName = fullName,
            details = details,
            imageUrl = this.imageUrl,
            matchScoreText = matchScoreFormatted,
            education = "Education: ${this.education ?: "N/A"}", // Handle nullability
            religion = "Religion: ${this.religion ?: "N/A"}", // Handle nullability
            status = this.status,
            showActions = showActionButtons
        )
    }
}