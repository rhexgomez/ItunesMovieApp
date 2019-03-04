


<img src="/images/previewgif.gif" width="200" align="left" hspace = "20">

# ItunesMovieApp

A sample app that consume the [Itune API Search API](https://affiliate.itunes.apple.com/resources/documentation/itunes-store-web-service-search-api/). 
This demo app only fetches MOVIES that matches the search keyword. 

This app is using [Model–view–viewmodel](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) architecture because
we wanted to follow the [single responsibility principle](https://en.wikipedia.org/wiki/Single_responsibility_principle),
with this approach it makes the app more maintainable and easier to test since we can mock each part.

It also caches the results after consuming the API with that it makes the searching a lot efficient since we do not need
to perform a similar request (save some bandwidth) plus the cached movies should be viewable offline except 
the Video playback.

See also the app the [DOCUMENTATION](https://rhexgomez.github.io/ItunesMovieApp/app/index.html) rendered by 
[Dokka](https://github.com/Kotlin/dokka).
