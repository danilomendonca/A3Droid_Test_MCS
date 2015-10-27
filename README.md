# A3Droid Test Applications: Media Content Sharing

Currently alljoyn framework only supports wi-fi as a stable connection method. Bluetooth has been disabled and wi-fi direct is still experimental. Make sure all devices are in the same wi-fi local area network.

## Required libraries

* [Alljoyn for Android](https://allseenalliance.org/framework/download) .jar and the .so - armeabi folder, in case of devices ~~and arme VDM, x86 folder, in case of x86 VDM~~
* A3Droid .jar
* Android v4 .jars

## Experiment steps

* Select two or more devices to be the receiver group members
* Use one of the devices interface to create a group
  * Insert the numeric ID of the group in the text field
  * Click in the **Create Group** button
  * Wait for all devices to print the message **A3Test_GROUP_ID_[FOL|SUP]Role**
* Follow the instruction for the [Media Content Sender](https://github.com/danilomendonca/A3Droid_Test_MCS_Sender) application
* Once the sender has started sending messages, each group member will print log messages indicating the arrival of the hand-shake message and also the media content message
* Experiment measurements are saved in the sender device
