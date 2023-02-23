# About the project

Sticky Notes is an application for Android platform which allows users to 
make some notes and store 'em locally. The app has a lot of great features 
for management, storing and editing of user notes. 

### What's happening there?

At first, this was one of my curriculum projects, made in 2016. The app was 
a client for the backend [^1]. 

[^1]: Backend was made with `Python`+`Flask`+`PostgreSQL`

Besides notes, the app had such features as contacts management (something 
like "Friends" in social networks), notes sharing and shared editing. Also, 
I had planned to add comments functionality for shared notes, but then 
I abandoned this project for several years. 

I started my software engineering career then, so I didn't have so much 
free time (and desire) to continue the project [^2]. 

[^2]: My work turned out to be much more interesting 😉

After almost 7 years, in the end of 2022, I decided to turn the Sticky Notes 
into a small portfolio project 🙂

### Features

* Local notes storage
* (_TBD_) Google Drive sync
* Tags
* (_TBD_) Search
* (_TBD_) Pinned notes
* (_TBD_) Geotagging for notes
* (_TBD_) Mutli-Tasking and Secondary Screen support
* (_TBD_) Storage Access Framework support

What is planned to be done you can check out in the 
[current TODO](https://github.com/alexeychurchill/sticky-notes-client/issues/6) 
issue. You also can checkout **Issues** tab in general. Especially, all issues 
tagged by ![label:feature](https://img.shields.io/github/labels/alexeychurchill/sticky-notes-client/feature) tag.

# Tech Stack

* Kotlin
* Android SDK
* Hilt
* Room
* Jetpack
  * ViewModel
  * Navigation
  * Lifecycle libs
* Jetpack Compose
  * Material 3
  * Material Icons
  * Utility libs for: `ViewModel`s, `Hilt`, `Lifecycle`, `Navigation`

# Licence

```
Copyright 2023 Oleksii L. (aka alexeychurchill)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
