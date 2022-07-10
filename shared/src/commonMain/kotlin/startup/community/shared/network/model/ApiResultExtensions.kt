package startup.community.shared.network.model

fun <DataIn, DataOut> ApiResult<DataIn>.map(
    onData: (data: DataIn) -> DataOut
): ApiResult<DataOut> {
    return when (this) {
        is ApiResult.Success -> ApiResult.Success(onData(this._data!!))
        is ApiResult.Error -> ApiResult.Error(this.exception)
    }
}


