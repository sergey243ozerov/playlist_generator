package network

class AccessTokenHolder {

    private var accessToken : String? = null
    fun getAccessToken(): String{
        return requireNotNull(accessToken)
    }

    fun saveAccessToken(token: String){
        accessToken = token
    }

    fun clearAccessToken(){
        accessToken = null
    }
}