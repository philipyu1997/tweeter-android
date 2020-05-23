# Tweeter

## Table of Contents
1. [Overview](#Overview)
2. [Product Specs](#Product-Specs)
3. [App Walkthrough](#App-Walkthrough)
4. [APIs](#APIs)
5. [Libraries](#Libraries)
6. [Credits](#Credits)

## Overview
### Description

Tweeter is a simple Twitter client that allows users to view their feed and compose, favorite, and retweet tweets.

## Product Specs
### User Stories

- [x] User shall be able to sign in to Twitter using OAuth login.
- [x] User shall be able to view tweets from their home timeline.
  - [x] User is displayed the username, name, and body for each tweet.
  - [x] User is displayed the relative timestamp for each tweet "8m", "7h".
- [x] User shall be able to refresh tweets timeline by pulling down to refresh.
- [x] User shall be able to view more tweets as they scroll with infinite pagination.
- [ ] User shall be able to tap a tweet to display a "detailed" view of that tweet.
- [ ] On the Twitter timeline, leverage the CoordinatorLayout to apply scrolling behavior that hides / shows the toolbar.
- [ ] Replace all icon drawables and other static image assets with vector drawables where appropriate.
- [ ] User shall be able to see an indeterminate progress indicator when any background or network task is happening.
- [ ] User shall be able to see embedded image media within a tweet on list or detail view.
- [ ] User shall be able to click a link within a tweet body on tweet details view. The click will launch the web browser with relevant page opened.
- [ ] User shall be able to view following / followers list through any profile they view.
- [x] User shall be able to compose and post a new tweet.
  - [x] User shall be able to click a “Compose” icon in the Action Bar on the top right.
  - [x] User shall be able to then enter a new tweet and post this to Twitter.
  - [x] User shall be taken back to home timeline with new tweet visible in timeline.
  - [x] Newly created tweet shall be manually inserted into the timeline and not rely on a full refresh.
  - [x] User shall be able to see a counter with total number of characters left for tweet on compose tweet page.
- [ ] User shall be able to select "reply" from detail view to respond to a tweet.
  - [ ] User that wrote the original tweet shall automatically "@" replied in compose.
- [ ] User shall be able to take favorite (and unfavorite) or retweet actions on a tweet.
- [ ] Compose tweet functionality shall be build using modal overlay.
- [ ] Use Parcelable instead of Serializable using the popular Parceler library.
- [ ] Use the popular ButterKnife annotation library to reduce view boilerplate.
- [ ] User shall be able to open the Twitter app offline and see last loaded tweets. Persisted in SQLite tweets are refreshed on every application launch. While "live data" is displayed when app can get it from Twitter API, it is also saved for use in offline mode.

## App Walkthrough

Here's a GIF of how the app works:

<img src="ADD_GIF_LINK" width=250><br>

## APIs

- [Twitter API](https://developer.twitter.com/en) - Allow developers to access core Twitter data — including update timelines, status data, and user information.

## Libraries

- [Android Async HTTP](https://github.com/codepath/CPAsyncHttpClient) - Simple asynchronous HTTP requests with JSON parsing.
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android.

## Credits

>This is a companion project to CodePath's Professional Android Course, check out the full course at [www.codepath.org](https://codepath.org/).
