# StorageSystem
Personal Storage Management System

This is a "small" personal project where i'll use Java, JavaFX, the Gson-Libary, Apache-Web-Server, JSON-server and Outpan.com as Barcode libary.
I want to build an application, with which i can manage the items in my basement (preferably groceries).
I'll try to keep the setup costs as low as possible, so it'll be a system everyone can build.
I have to admit, that it got more expensive than i thought. (You could decrease the price by just buying a Pi (35€) and a smaller screen (30-45€) to about 100€).

Physical parts are: 
  - TaoTronics USB Laser-Barcodescanner (23€)
  - RaspberryPI 3 StarterKit which includes: (100€)
      - Raspberry Pi 3
      - Your Choice of Pibow 3
      - 8GB NOOBs microSD card
      - Chiclet Keyboard
      - Optical Mouse
      - Official Raspberry Pi 2A PSUn>
      - HDMI cable
      - Raspberry Pi Stickers
  - Tontec 7 inch High Resolution 1024 * 600 Screen Display LCD TFT Monitor(55€)

------------------------------
Total costs: 178€

![alt tag] (https://github.com/timgrossmann/StorageSystem/blob/master/img/storageSystem_V1_1.png)
- UI of the JavaFX App


![alt tag] (https://raw.githubusercontent.com/timgrossmann/StorageSystem/master/img/WebView.png)
- Very Simple but responsive WebPage of the Items


![alt tag](http://ecx.images-amazon.com/images/I/71T55P9USGL._SL1500_.jpg)
![alt tag] (http://ecx.images-amazon.com/images/I/51RxfzoqFXL._SY300_.jpg)
![alt tag] (http://ecx.images-amazon.com/images/I/61qFVwEcWrL._SL1200_.jpg)


<h2>Documentation of the problems with solutions</h2>
<h4><u>Running JavaFX with Maven</u></h4>
<p>The first small problem was to get JavaFX running with Maven. Since i wanted to use the Logging-Framework Log4J, Maven was pretty easy to use and a good choice. <br />
Problem : Deploying JavaFX as .jar to be executed via the commanline <br />
Solution: Using the "JavaFX Maven Plugin" from https://github.com/javafx-maven-plugin/javafx-maven-plugin</p>

<br />

<h4><u>Running the JavaFX .jar on PI</u></h4>
<p>Starting a JavaFX Application on a RaspberryPi isn't that easy, since Oracle removed JFX from their ARM install. <br />
Problem : Installing Java ARM and getting JavaFX seperately <br />
Solution: Installing the latest Oracle Java ARM version, removing all the other Java installations (making sure the now installed one is used). Downloading the "JavaFX Embedded SDK" here: http://gluonhq.com/labs/javafxports/downloads/ and extracting the parts in the corresponding folders of the oracle Java version.</p>

<p>Will follow soon...</p>
