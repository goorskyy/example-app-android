/*
 * Copyright (c) 2016-2021 Onegini B.V.
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

package com.onegini.mobile.exampleapp;

import android.os.Build;
import com.onegini.mobile.sdk.android.model.OneginiClientConfigModel;

@SuppressWarnings("unused")
public class OneginiConfigModel implements OneginiClientConfigModel {

  private final String appIdentifier = "ExampleApp";
  private final String appPlatform = "android";
  private final String redirectionUri = "oneginiexample://loginsuccess";
  private final String appVersion = "7.5.0";
  private final String baseURL = "https://demo-msp.onegini.com";
  private final String resourceBaseURL = "https://demo-msp.onegini.com/resources/";
  private final String keystoreHash = "cdf78d87c0077247448535103aa333fecac18ae6a074e051ed8c331ee9581061";
  private final String serverPublicKey = "BBFFCC164C1AC014069B6C2C96E012E434D064F1EB736EF0E2470EFF15410D9D";

  @Override
  public String getAppIdentifier() {
    return appIdentifier;
  }

  @Override
  public String getAppPlatform() {
    return appPlatform;
  }

  @Override
  public String getRedirectUri() {
    return redirectionUri;
  }

  @Override
  public String getAppVersion() {
    return appVersion;
  }

  @Override
  public String getBaseUrl() {
    return baseURL;
  }

  @Override
  public String getResourceBaseUrl() {
    return resourceBaseURL;
  }

  @Override
  public int getCertificatePinningKeyStore() {
    return R.raw.keystore;
  }

  @Override
  public String getKeyStoreHash() {
    return keystoreHash;
  }

  @Override
  public String getDeviceName() {
    return Build.BRAND + " " + Build.MODEL;
  }

  @Override
  public String getServerPublicKey() {
    return serverPublicKey;
  }

  @Override
  public String toString() {
    return "ConfigModel{" +
        "  appIdentifier='" + appIdentifier + "'" +
        ", appPlatform='" + appPlatform + "'" +
        ", redirectionUri='" + redirectionUri + "'" +
        ", appVersion='" + appVersion + "'" +
        ", baseURL='" + baseURL + "'" +
        ", resourceBaseURL='" + resourceBaseURL + "'" +
        "}";
  }
}
