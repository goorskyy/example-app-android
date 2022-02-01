# Setup instruction

If you want to see working example app using Onegini SDK then you can open Example App project in Android Studio to test it live.

## Custom configuration

When you need to configure app with your instance of Token Server then please look into documentation and follow the steps:

1. Configure Token server: https://docs.onegini.com/public/token-server/
2. Download zip file with your app’s configuration
3. Set your artifactory_user, artifactory_password and artifactory_contextUrl in gradle.properties
4. Open the Example App project
5. Use SDK Configurator ( https://github.com/Onegini/sdk-configurator ) to generate your own keystore.bks and OneginiConfigModel class 
   (those files already exist in Example App and will be overridden by SDK Configurator).
6. You're ready!
