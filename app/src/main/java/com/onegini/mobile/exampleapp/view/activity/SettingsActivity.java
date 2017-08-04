/*
 * Copyright (c) 2016-2017 Onegini B.V.
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

package com.onegini.mobile.exampleapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.network.fcm.FCMRegistrationService;
import com.onegini.mobile.exampleapp.storage.DeviceSettingsStorage;
import com.onegini.mobile.exampleapp.util.DeregistrationUtil;
import com.onegini.mobile.sdk.android.client.UserClient;
import com.onegini.mobile.sdk.android.handlers.OneginiChangePinHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthWithPushEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiChangePinError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthEnrollmentError;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthWithPushEnrollmentError;
import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class SettingsActivity extends AppCompatActivity {

  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.toolbar)
  Toolbar toolbar;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_mobile_authentication)
  Button mobileAuthButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_mobile_authentication_push)
  Button mobileAuthPushButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_change_pin)
  Button changePinButton;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.button_change_authentication)
  Button changeAuthentication;
  @SuppressWarnings({ "unused", "WeakerAccess" })
  @Bind(R.id.retrofit_radio)
  RadioGroup retrofitRadio;
  @Bind(R.id.message)
  TextView message;
  @SuppressWarnings({ "unused", "WeakerAccess" })

  private DeviceSettingsStorage deviceSettingsStorage;
  private UserProfile authenticatedUserProfile;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);
    ButterKnife.bind(this);

    deviceSettingsStorage = new DeviceSettingsStorage(this);
    authenticatedUserProfile = OneginiSDK.getOneginiClient(this).getUserClient().getAuthenticatedUserProfile();

    retrofitRadio.check(deviceSettingsStorage.shouldUseRetrofit2() ? R.id.retrofit_2 : R.id.retrofit_1);
    retrofitRadio.setOnCheckedChangeListener((group, checkedId) -> deviceSettingsStorage.setShouldUseRetrofit2(checkedId == R.id.retrofit_2));
  }

  @Override
  protected void onResume() {
    super.onResume();
    setupView();
  }

  @Override
  public boolean onOptionsItemSelected(final MenuItem item) {
    switch (item.getItemId()) {
      // Respond to the action bar's Up/Home button
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void setupView() {
    setupActionBar();
    setupMobileAuthButtons();
  }

  private void setupMobileAuthButtons() {
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_off);
    mobileAuthPushButton.setEnabled(false);
    mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_not_available);

    final UserClient userClient = OneginiSDK.getOneginiClient(this).getUserClient();
    if (userClient.isUserEnrolledForMobileAuth(authenticatedUserProfile)) {
      onMobileAuthEnabled();
    }
  }

  private void setupActionBar() {
    setSupportActionBar(toolbar);

    final ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setLogo(R.mipmap.ic_launcher);
      actionBar.setDisplayUseLogoEnabled(true);
      actionBar.setDisplayShowTitleEnabled(false);
    }
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_mobile_authentication)
  public void enrollMobileAuthentication() {
    final OneginiMobileAuthEnrollmentHandler mobileAuthEnrollmentHandler = new OneginiMobileAuthEnrollmentHandler() {
      @Override
      public void onSuccess() {
        onMobileAuthEnabled();
        showToast("Mobile authentication enabled");
      }

      @Override
      public void onError(final OneginiMobileAuthEnrollmentError error) {
        @OneginiMobileAuthEnrollmentError.MobileAuthEnrollmentErrorType final int errorType = error.getErrorType();
        if (errorType == OneginiMobileAuthEnrollmentError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
        }

        showToast("Mobile authentication error - " + error.getMessage());
      }
    };
    OneginiSDK.getOneginiClient(this).getUserClient().enrollUserForMobileAuth(mobileAuthEnrollmentHandler);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_mobile_authentication_push)
  public void enrollMobileAuthenticationWithPush() {
    final OneginiMobileAuthWithPushEnrollmentHandler mobileAuthWithPushEnrollmentHandler = new OneginiMobileAuthWithPushEnrollmentHandler() {
      @Override
      public void onSuccess() {
        showToast("Mobile authentication enabled");
        mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_on);
      }

      @Override
      public void onError(final OneginiMobileAuthWithPushEnrollmentError error) {
        @OneginiMobileAuthWithPushEnrollmentError.MobileAuthWithPushEnrollmentErrorType final int errorType = error.getErrorType();
        if (errorType == OneginiMobileAuthWithPushEnrollmentError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
        }

        showToast("Mobile authentication error - " + error.getMessage());
      }
    };
    final FCMRegistrationService FCMRegistrationService = new FCMRegistrationService(this);
    FCMRegistrationService.enrollForPush(mobileAuthWithPushEnrollmentHandler);
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_change_pin)
  public void startChangePinFlow() {
    OneginiSDK.getOneginiClient(this).getUserClient().changePin(new OneginiChangePinHandler() {
      @Override
      public void onSuccess() {
        message.setText(R.string.change_pin_finished_succesfully);
      }

      @Override
      public void onError(final OneginiChangePinError oneginiChangePinError) {
        @OneginiChangePinError.ChangePinErrorType int errorType = oneginiChangePinError.getErrorType();
        if (errorType == OneginiChangePinError.USER_DEREGISTERED) {
          userDeregistered();
        } else if (errorType == OneginiChangePinError.DEVICE_DEREGISTERED) {
          new DeregistrationUtil(SettingsActivity.this).onDeviceDeregistered();
        }
        message.setText(oneginiChangePinError.getMessage());
      }
    });
  }

  @SuppressWarnings("unused")
  @OnClick(R.id.button_change_authentication)
  public void changeAuthentication() {
    startActivity(new Intent(this, SettingsAuthenticatorsActivity.class));
  }

  private void userDeregistered() {
    new DeregistrationUtil(this).onUserDeregistered(authenticatedUserProfile);

    final Intent intent = new Intent(this, LoginActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);
    finish();
  }

  private void showToast(final String message) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
  }

  private void onMobileAuthEnabled() {
    mobileAuthButton.setText(R.string.settings_mobile_enrollment_on);
    final int googlePlayServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
    if (googlePlayServicesAvailable == ConnectionResult.SUCCESS) {
      mobileAuthPushButton.setEnabled(true);
      mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_off);
      if (OneginiSDK.getOneginiClient(this).getUserClient().isUserEnrolledForMobileAuthWithPush(authenticatedUserProfile)) {
        mobileAuthPushButton.setText(R.string.settings_mobile_push_enrollment_on);
      }
    }
  }
}
