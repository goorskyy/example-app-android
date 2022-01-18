/*
 * Copyright (c) 2016-2020 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.exampleapp.network;

import android.content.Context;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import com.onegini.mobile.exampleapp.network.client.UserClient;
import com.onegini.mobile.exampleapp.network.response.DevicesResponse;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserService {

  private static UserService INSTANCE;
  private final UserClient userRetrofitClient;

  private UserService(final Context context) {
    userRetrofitClient = SecureResourceClient.prepareSecuredUserRetrofitClient(UserClient.class, context);
  }

  public static UserService getInstance(final Context context) {
    if (INSTANCE == null) {
      INSTANCE = new UserService(context);
    }
    return INSTANCE;
  }

  public Single<DevicesResponse> getDevices() {
    return userRetrofitClient.getDevices()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
