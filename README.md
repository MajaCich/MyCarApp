## [ Opis po polsku - English description below]

## Przykkładowy projekt wykorzystujący Car App Library w Javie

Projekt ten jest przeznaczony wyłącznie na platformę Android Auto i prezenruje jak używać i tworzyć aplikacje w Javie przy uzyciu Car App Library, aby tworzyć aplikacje samochodowe. Zawiera przykładowe użycie komponentów, szablonów i interfejsów dostarczonych przez bibliorekę, a także implementacje sterowania głosowego w module CarVoiceCommands.

# Funkcje

Projekt ten pokazuje użycie:
- Szablonów: przykłady jak używać szablonów. Lista szablonów: Message, LongMessage, List (and selectable list), Grid, Pane, Tab, SignIn, Search.
- Komponenty takie jak CarIcon, CarSpan i CarColor: przykładowe użycie tych klas.
- CarInfo i CarSensors: interfejsy, które dają dostęp do informacji o pojeździe takich jak model, marka, rok, typ paliwa i złącza ev. Dodatkowo daje dostęp do sensorów pojazdu takicj jak żyroskop, kompas, akcelerometr i danych o prędkości, przebiegu
- CarAudioRecord: przykład wykorzystania klasy CarAudioRecord do nagrywania dźwięku z mikforonu pojazdu
- Sterowanie głosowe: implementacja sterowania głosowego wykorzystująca SpeechRecognizer do rozpoznawania mowy i CarAudioRecord do odczytywania dźwięku z mikforonu pojazdu zamiast domyślnego mikrofonu urządzenia mobilnego 

# Jak uruchomić projekt bez posiadania prawdziego pojazdu?

Przez Android Studio można pobrać Desktop-Head-Unit, który symuluje ekran pojazdu wyposażonego w Android Auto.
Urządzenie mobile należy podłączyć do komputera przez USB lub Wi-Fi. Więcej informacji: **https://developer.android.com/tools/adb#connect-to-a-device-over-wi-fi**
Następnie uruchomić trzeba konsolę i z lini komend przejść do folderu gdzie znajduję się DHU.
Więcej o tym jak uruchomić DHU: **https://developer.android.com/training/cars/testing/dhu**



## [English Description]
## Car App Library Example Project in Java

This project is specifically designed for Android Auto and  demonstrates the use of the Android Car App Library to create in-car applications using Java. It includes examples of components, templates, and interfaces provided by the library, as well as the implementation of a voice command handling module CarVoiceCommands

# Features

This project showcaes:
- Templates: examples how to use templates. List of templates: Message, LongMessage, List (and selectable list), Grid, Pane, Tab, SignIn, Search. 
- Components like CarIcon, CarSpan and CarColor: examples of these classess
- CarInfo and CarSensors: interfaces that gives access to car info like model, make, year, fuel type and ev connectors. Also give access to sensors like gyroscope, compass, accelerometer and speed, mileage, energy level information.
- CarAudioRecord: an example of how to utilize CarAudioRecord to access car built-in microphone
- Voice command handling: implements voice control using SpeechRecognizer for speech recognition and CarAudioRecord to read data from car's microphone instead of mobile phone's microphone

# How to run this project without a car?

From Android Studio SDK Tools download Desktop-Head-Unit, that emulates car display with Android Auto. 
Connect mobile phone to development device via USB or over Wi-Fi. Read more here: **https://developer.android.com/tools/adb#connect-to-a-device-over-wi-fi**
Open command line
Read more about how to run DHU here: **https://developer.android.com/training/cars/testing/dhu**

