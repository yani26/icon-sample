# sample code - connection & QRcode
## Description
Show connection status and QRcode on menu 

## Architecture
<!--sequence
Note right of DevicePulseActivity: show QRcode
DeviceManager->DevicePulseActivity:control status
Note right of DeviceManager:set online icon
DeviceManager->Server:connect
Note right of DeviceManager:set offline icon
DeviceManager-Server:disconnect-->
![](https://i.imgur.com/qGJ6BAX.png)

## Files
* Const
* DeviceConnectEvent
* DeviceDisconnectEvent
* DeviceInitResuktEvent
* DeviceManager
* DevicePulseActivity
* DeviceQRcodeResultEvent
* DeviceServiceReadyEvent
* DeviceUserListResuktEvent
* User
* MainActivity

## screenshot
![](https://i.imgur.com/4ozVe1e.png | width=200)


