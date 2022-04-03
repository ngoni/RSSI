# RSSI App

**Brief App Overview** <br/>
This is a demo app that uses a Foreground Service to perform long running Wifi Scans
and sends this data to an imaginary Api. <br/>

The WorkManager was used to schedule these periodic Wifi Scans for RSSI values. <br/>

The UI is meant to display the latest queried Received Signal Strength Indicator values, and the corresponding SSID name. <br/>

**Solution Considerations**<br/>
MVVM Architecture Pattern<br/>
Segregation of into the following layers:<br/>
(1) UI Layer - this is where we have views like Activities/Fragments <br/>
(2) Domain Layer - this is where we have ViewModels <br/>
(3) Data Layer - this is where we have our repositories and data sources (WifiManager)<br/>
Ability to test the above mentioned layers<br/>
Use of WorkManager for scheduling tasks<br/>

**Mechanism for demonstrating the application making requests** <br/>
Two approaches have been used:<br/>
(1) The RSSI values are displayed in the MainActivity UI within 15 minutes (this is the min time WorkManager enforces between tasks) <br/>
(2) Check Logcat statements using the filter: "OkHttpClient". The Json payload for the network request and response are logged.<br/>

**Testing Instructions** <br/>

In Android Studio, find the "test" folder which exists under "src" directory <br/>
Right click this folder and select "Run 'Tests in 'RSSI.app'" <br/>

Alternatively run the below command in the Terminal: <br/>
./gradlew test <br/>