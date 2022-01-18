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
import com.onegini.mobile.exampleapp.model.ApplicationDetails;
import com.onegini.mobile.exampleapp.network.client.AnonymousClient;
import com.onegini.mobile.exampleapp.network.client.SecureResourceClient;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AnonymousService {

  private static AnonymousService INSTANCE;
  private final AnonymousClient applicationDetailsRetrofitClient;

  private AnonymousService(final Context context) {
    applicationDetailsRetrofitClient = SecureResourceClient.prepareSecuredAnonymousRetrofitClient(AnonymousClient.class, context);
  }

  public static AnonymousService getInstance(final Context context) {
    if (INSTANCE == null) {
      INSTANCE = new AnonymousService(context);
    }
    return INSTANCE;
  }

  public Single<ApplicationDetails> getApplicationDetails() {
    return applicationDetailsRetrofitClient.getApplicationDetails()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }
}
