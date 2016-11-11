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
// TODO () Make sure you've imported the jobdispatcher.JobService, not job.JobService

// TODO () Add a class called SunshineFirebaseJobService that extends jobdispatcher.JobService

//  TODO () Declare a Thread field called mFetchWeatherThread

//  TODO () Override onStartJob and within it, spawn off a separate thread to sync weather data
//              TODO () Once the weather data is sync'd, call jobFinished with the appropriate arguments

//  TODO () Override onStopJob, interrupt the thread and set it to null and return true
