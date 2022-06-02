package ss.team16.nthulostfound.data.repository

import ss.team16.nthulostfound.domain.model.UserData
import ss.team16.nthulostfound.domain.repository.UserRepository

class UserRepositoryMockImpl: UserRepository {
    private var mockUser = UserData(
        null,
        "",
        "",
        "なまえ",
        "109000000",
        "nthu@example.com"
    )

    override suspend fun getUser(): UserData {
        return mockUser
    }

    override suspend fun saveUser(user: UserData) {
        mockUser = user
    }
}