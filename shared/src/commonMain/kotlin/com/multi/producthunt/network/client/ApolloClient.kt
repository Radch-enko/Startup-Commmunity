package com.multi.producthunt.network.client

import com.apollographql.apollo3.ApolloClient
import com.multi.producthunt.utils.AppConfig

expect fun apolloClient(appConfig: AppConfig): ApolloClient
