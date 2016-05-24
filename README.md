Play Silhouette Reactive Mongo Angular Seed Project
===================================================

The Play Silhouette Reactive Mongo Angular Seed project shows how [Silhouette](https://github.com/mohiva/play-silhouette) can be used
to create a SPA with [AngularJS](https://angularjs.org/)/[Satellizer](https://github.com/sahat/satellizer) and Play
scaffolded by [yeoman](https://github.com/tuplejump/play-yeoman) and how you can presisit the users with [MongoDb](https://www.mongodb.org). It's a starting point which can be extended to fit your needs.

This project is a mix of these two projects:

1. [play-silhouette-angular-seed](https://github.com/mohiva/play-silhouette-angular-seed)
2. [play-silhouette-reactivemongo-seed](https://github.com/ezzahraoui/play-silhouette-reactivemongo-seed)

## Example

[![Deploy to Heroku](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

(The "Build App" phase will take a few minutes)

## Features

* Sign Up
* Sign In (Credentials)
* JWT authentication
* Social Auth (Facebook, Google+, VK, Twitter, Xing)
* Dependency Injection with Guice
* Publishing Events
* Avatar service
* Remember me functionality
* [Security headers](https://www.playframework.com/documentation/2.4.x/SecurityHeaders)
* [CSRF Protection](https://www.playframework.com/documentation/2.4.x/ScalaCsrf)
* [Fix duplicating the same user multiple times in the db bug](https://github.com/ezzahraoui/play-silhouette-reactivemongo-seed/pull/2).

## Documentation

Consulate the [Silhouette documentation](http://silhouette.mohiva.com/docs) for more information. If you need help with the integration of Silhouette into your project, don't hesitate and ask questions in our [mailing list](https://groups.google.com/forum/#!forum/play-silhouette) or on [Stack Overflow](http://stackoverflow.com/questions/tagged/playframework).

## Getting started

1. [Install MONGODB](https://docs.mongodb.org/v3.0/tutorial/#installation)
   
   Then create a db called `silhouette`.

2. Make sure u have [Ruby](https://www.ruby-lang.org/de/) and [node.js](http://nodejs.org/) installed.

  Then you must install the node packages [yo](http://yeoman.io), [grunt](http://gruntjs.com/) and [bower](http://bower.io/):

  ```
  npm install -g yo grunt grunt-cli bower
  ```

  And the ruby packages [sass](http://sass-lang.com/) and [compass](http://compass-style.org/):

  ```
  gem install sass compass
  ```

  Alternative you can use Bundler to install the ruby packages:

  ```
  bundle install -j4 --path .bundle
  ```
3. Configure social providers 
  
  To configure the social providers for [Satellizer](https://github.com/sahat/satellizer), Open the file "~\ui\app\scripts\app.js" and input your providers `clientId`: 
  ```
  // Facebook
  $authProvider.facebook({
    clientId: 'your-client-id',
    ...
  });
  
  // Google
  $authProvider.google({
    clientId: 'your-client-id',
    ...
  });
  ...
  ```
  If you are using Heroku Update the "~\app.json" file with your client secret and client ID.
  ```
  "env": {
    "BUILDPACK_URL": "https://github.com/heroku/heroku-buildpack-multi",
    "NPM_CONFIG_PRODUCTION": "false",
    "PLAY_CONF_FILE": "application.conf",
    "PLAY_APP_SECRET": "changeme",
    "FACEBOOK_CLIENT_ID": "",
    "FACEBOOK_CLIENT_SECRET": "",
    "GOOGLE_CLIENT_ID": "",
    "GOOGLE_CLIENT_SECRET": "",
    "TWITTER_CONSUMER_KEY": "",
    "TWITTER_CONSUMER_SECRET": ""
  }
  ```
  
  To test social providers on localhost, you can either set your system environment variables as defined in the app.json "env" section or manually update the "~\conf\silhouette.conf" file directly with your client ID and client secret.
  ```
  # Google provider
  google.accessTokenURL="https://accounts.google.com/o/oauth2/token"
  google.redirectURL="http://localhost:9000"
  google.clientID="your-client-id"
  google.clientSecret="your-client-secret"
  google.scope="profile email"
  ```

4. Start sbt and run the following:

  ```
  $ update

  $ npm install

  $ bower install

  $ grunt build

  $ run
  ```

# License

The code is licensed under [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0).
