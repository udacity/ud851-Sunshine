/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// TODO (2) Make sure you've imported the jobdispatcher.JobService, not job.JobService

// TODO (3) Add a class called SunshineFirebaseJobService that extends jobdispatcher.JobService

//  TODO (4) Declare an ASyncTask field called mFetchWeatherTask

//  TODO (5) Override onStartJob and within it, spawn off a separate ASyncTask to sync weather data
//              TODO (6) Once the weather data is sync'd, call jobFinished with the appropriate arguments

//  TODO (7) Override onStopJob, cancel the ASyncTask if it's not null and return true
